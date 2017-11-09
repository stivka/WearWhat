import inputOutput.ReaderWriter;
import org.json.JSONArray;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReaderWriterTests {


    @Test
    public void doesWriterAppendToFile() {
        String fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/writingTestOutput.txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
            ReaderWriter.appendToFile("test", fileName);
            JSONArray lines = ReaderWriter.readAllLinesFromFile(fileName);
            assertEquals("test", lines.getString(0));
            file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doesReaderGetAllRowsFromInputFile() {
        String fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/input.txt";
        int numberOfFilesInGivenDir = new File("/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/cities").list().length;
        JSONArray jsonArray = ReaderWriter.readAllLinesFromFile(fileName);
        int lineNumberCount = jsonArray.length();
        assertEquals(lineNumberCount, numberOfFilesInGivenDir);
    }

    @Test
    public void canReaderReadInputFilesSyntax() {
        String fileName = "/Users/Stiv/IdeaProjects/idk0051/Automaattestimine/src/inputOutput/readingFromInputTest.txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
            ReaderWriter.appendToFile("test, test, test", fileName);
            List list = ReaderWriter.readLineFromFileAndSplitByComma(fileName);
            assertEquals(list.size(), 3);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
