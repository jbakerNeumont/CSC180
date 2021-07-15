import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void doesHTMLExist() {
		File html = new File("data\\300.html");
		assertEquals(html.exists(), true);
	}
	
	@Test
	void doesPDFExist() {
		File pdf = new File("data\\300.pdf");
		assertEquals(pdf.exists(), true);
	}
	 
}
