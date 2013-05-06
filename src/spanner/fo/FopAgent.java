/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner.fo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.tidy.Tidy;
import spanner.WKException;

/**
 *
 * @author lim16
 */
public class FopAgent {

    public static void preview(String htmlContent, String dst) throws WKException {
        OutputStream out = null;
        try {
            InputStream htmlStream = new ByteArrayInputStream(htmlContent.getBytes("utf-8"));

            File tidy = File.createTempFile("spanner_", ".html");
            OutputStream fos = new FileOutputStream(tidy);
            Tidy T = new Tidy();
            T.setTidyMark(false);
            T.setWord2000(true);
            T.setXmlOut(true);
            T.parseDOM(htmlStream, fos);
            fos.close();

            htmlStream = new FileInputStream(tidy);

            InputStream xsltStream = FopAgent.class.getResourceAsStream("xhtml-to-xslfo.xsl");

            out = new BufferedOutputStream(new FileOutputStream(new File(dst)));

            FopAgent.preview(htmlStream, xsltStream, out);
        } catch (Exception ex) {
            throw new WKException(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(FopAgent.class.getName()).log(Level.SEVERE, null, ex);
                    throw new WKException(ex);
                }
            }
        }
    }

    public static void preview(InputStream html, InputStream xsl, OutputStream out) throws WKException {
        try {
            FopFactory fopFactory = FopFactory.newInstance();
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
//            Transformer transformer = factory.newTransformer(); // identity transformer
            Source xslt = new StreamSource(xsl);
            Transformer transformer = factory.newTransformer(xslt);

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            Source src = new StreamSource(html);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        } catch (Exception ex) {
            throw new WKException(ex);
        }
    }

    private static File writeToTempFile(String content) throws IOException {
        File temp = File.createTempFile("spanner_", "html");
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(content);
        out.close();
        return temp;
    }
}
