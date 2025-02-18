package XMLsignature.XMLsignature;

import org.apache.xml.security.Init;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.KeyValue;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class XMLSigner {

    static {
        Init.init();
    }

    public static KeyPair generateKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static void signXML(String inputFilePath, String outputFilePath, KeyPair keyPair) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(inputFilePath));

        // Check if the document already contains a signature
        NodeList signatureNodes = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "Signature");
        if (signatureNodes.getLength() > 0) {
            System.out.println("The document already contains a signature. Aborting signing process.");
            return;
        }

        // Create an XMLSignature object without prefix
        XMLSignature sig = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_DSA);

        // Add the signature element to the document
        Element rootElement = doc.getDocumentElement();
        rootElement.appendChild(sig.getElement());

        // Create and add transforms
        Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

        // Add KeyInfo without namespace prefix and declaration
        KeyInfo keyInfo = new KeyInfo(doc);
        keyInfo.add(new KeyValue(doc, keyPair.getPublic()));
        Element keyInfoElement = keyInfo.getElement();
        keyInfoElement.removeAttribute("xmlns:ds");
        sig.getElement().appendChild(keyInfoElement);

        // Update CanonicalizationMethod
        Element canonicalizationMethod = (Element) doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "CanonicalizationMethod").item(0);
        canonicalizationMethod.setAttribute("Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");

        // Remove the ds: prefix and xmlns:ds declaration
        removeNamespacePrefixAndDeclaration(doc);

        // Sign the document
        sig.sign(keyPair.getPrivate());

        // Save the signed document to file
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(outputFilePath)));
        System.out.println("XML document signed successfully.");
    }

    private static void removeNamespacePrefixAndDeclaration(Document doc) {
        Element sigElement = (Element) doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "Signature").item(0);
        sigElement.removeAttribute("xmlns:ds");
        removePrefixRecursively(sigElement);
    }

    private static void removePrefixRecursively(Element element) {
        element.setPrefix(null);
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if (element.getChildNodes().item(i) instanceof Element) {
                removePrefixRecursively((Element) element.getChildNodes().item(i));
            }
        }
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = generateKeys();

            String inputFilePath = "1.xml";
            String outputFilePath = "1_signed.xml";
            signXML(inputFilePath, outputFilePath, keyPair);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}