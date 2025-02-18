package XMLsignature.XMLsignature;

import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.security.PublicKey;

public class XMLVerifier {

    static {
        Init.init();
    }

    public static PublicKey extractPublicKey(String signedFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(signedFile));

        NodeList keyInfoNodes = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "KeyInfo");
        if (keyInfoNodes.getLength() == 0) {
            throw new Exception("No KeyInfo element found");
        }

        Element keyInfoElement = (Element) keyInfoNodes.item(0);
        XMLSignature sig = new XMLSignature((Element) keyInfoElement.getParentNode(), signedFile);

        return sig.getKeyInfo().getPublicKey();
    }

    public static boolean verifyXMLSignature(String inputFilePath, PublicKey publicKey) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(inputFilePath));

        NodeList nl = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "Signature");
        if (nl.getLength() == 0) {
            throw new Exception("No XML Digital Signature found, document is discarded");
        }

        Element signatureElement = (Element) nl.item(0);
        XMLSignature signature = new XMLSignature(signatureElement, "");

        return signature.checkSignatureValue(publicKey);
    }

    public static void main(String[] args) {
        try {
            // Extract public key from the signed XML document
            String inputFilePath = "signed_output.xml";
            PublicKey publicKey = extractPublicKey(inputFilePath);

            // Verify the XML document
            boolean isValid = verifyXMLSignature(inputFilePath, publicKey);

            if (isValid) {
                System.out.println("XML signature is valid.");
            } else {
                System.out.println("XML signature is invalid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}