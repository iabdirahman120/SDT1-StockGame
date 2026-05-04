import business.TradingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TradingServiceIntegrationTest {

    private static final String TEST_DIR = "testdata";

    private FileUnitOfWork uow;
    private FileStockDao stockDao;
    private FilePortfolioDao portfolioDao;
    private FileOwnedStockDao ownedStockDao;
    private TradingService service;

    @BeforeEach
    void setUp() throws Exception {
        new File(TEST_DIR).mkdirs();

        // Brug .txt — det er hvad FileUnitOfWork forventer
        writeFile(TEST_DIR + "/stocks.txt",      "AAPL|Apple|100.0|Steady");
        writeFile(TEST_DIR + "/portfolios.txt",  "1|10000.0");
        writeFile(TEST_DIR + "/ownedstocks.txt", "");

        uow           = new FileUnitOfWork(TEST_DIR);
        stockDao      = new FileStockDao(uow);
        portfolioDao  = new FilePortfolioDao(uow);
        ownedStockDao = new FileOwnedStockDao(uow);
        service       = new TradingService(uow, stockDao, portfolioDao, ownedStockDao);
    }


    @AfterEach
    void tearDown() throws Exception {
        // Slet testmappen efter hver test
        deleteDirectory(new File(TEST_DIR));
    }

    @Test
    void buyStock_gemmerIFil() throws Exception {
        // Act — køb 5 aktier til 100 kr = 500 + 2% fee = 510
        service.buyStock(1, "AAPL", 5);

        // Læs portfolio-filen og tjek balance er reduceret
        String portfolioIndhold = Files.readString(Path.of(TEST_DIR + "/portfolios.txt"));
        assertTrue(portfolioIndhold.contains("9490"),
                "Balance skal være 9490 efter køb (10000 - 510)");
    }

    @Test
    void sellStock_gemmerIFil() throws Exception {
        // Først køb aktier
        service.buyStock(1, "AAPL", 5);

        // Sælg 3 aktier: 3 * 100 - 2% fee = 294
        service.sellStock(1, "AAPL", 3);

        // Tjek at ownedstocks-filen stadig har 2 aktier tilbage
        String ownedIndhold = Files.readString(Path.of(TEST_DIR + "/ownedstocks.txt"));
        assertTrue(ownedIndhold.contains("2"),
                "Der skal være 2 aktier tilbage efter salg");
    }

    @Test
    void buyStock_ikkeNokPenge_ingenFil() throws Exception {
        // Prøv at købe 200 aktier til 100 kr = 20000+fee — mere end balance
        service.buyStock(1, "AAPL", 200);

        // Balance skal stadig være 10000
        String portfolioIndhold = Files.readString(Path.of(TEST_DIR + "/portfolios.txt"));
        assertTrue(portfolioIndhold.contains("10000"),
                "Balance må ikke ændres når der ikke er nok penge");
    }

    // Hjælpemetode til at skrive testfiler
    private void writeFile(String path, String content) throws Exception {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
        }
    }

    // Hjælpemetode til at slette testmappe rekursivt
    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) f.delete();
        }
        dir.delete();
    }
}
