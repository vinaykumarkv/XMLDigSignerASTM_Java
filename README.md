# XML Signature Application

This project provides a Java-based solution for signing and verifying XML documents. It consists of three primary Java files: `XMLSigner.java`, `XMLVerifier.java`, and `Main.java`. Below are the details of each component, their dependencies, and instructions for integrating each component with other systems.

## Components

### XMLSigner.java
This class handles the generation of a key pair and signing of XML documents.

**Key Methods:**
- `generateKeys()`: Generates a DSA key pair.
- `signXML(String inputFilePath, String outputFilePath, KeyPair keyPair)`: Signs an XML document using the given key pair.

**Dependencies:**
- Apache XML Security for Java (`org.apache.xml.security`): Required for cryptographic operations and handling XML signatures.
- Java Standard Library: For file handling, parsing XML, and cryptographic utilities.

### XMLVerifier.java
This class verifies the signature of signed XML documents.

**Key Methods:**
- `extractPublicKey(String signedFile)`: Extracts the public key from a signed XML document.
- `verifyXMLSignature(String inputFilePath, PublicKey publicKey)`: Verifies if the signature of an XML document is valid using the provided public key.

**Dependencies:**
- Apache XML Security for Java (`org.apache.xml.security`): Required for cryptographic operations and handling XML signatures.
- Java Standard Library: For file handling and parsing XML.

### Main.java
This class provides a graphical user interface (GUI) for signing and verifying XML documents.

**Key Methods:**
- `initialize()`: Initializes the GUI components.
- `chooseFile(JTextField textField)`: Opens a file chooser dialog to select a file.
- `SignButtonActionListener`: Action listener for the "Sign XML" button.
- `VerifyButtonActionListener`: Action listener for the "Verify XML" button.

**Dependencies:**
- Java Swing: For creating the GUI components and handling user interactions.
- `XMLSigner` and `XMLVerifier`: For signing and verifying XML documents, respectively.

## Integration

### Integrating XMLSigner with Other Systems
1. **Generate Keys:** Use the `generateKeys()` method to generate a key pair.
2. **Sign XML:** Use the `signXML(String inputFilePath, String outputFilePath, KeyPair keyPair)` method to sign an XML document. Provide the input XML file path, output file path, and the generated key pair.

### Integrating XMLVerifier with Other Systems
1. **Extract Public Key:** Use the `extractPublicKey(String signedFile)` method to extract the public key from a signed XML document.
2. **Verify Signature:** Use the `verifyXMLSignature(String inputFilePath, PublicKey publicKey)` method to verify the signature of an XML document. Provide the input XML file path and the extracted public key.

### Integrating Main with Other Systems
1. **Run the Application:** Execute the `Main` class to launch the GUI.
2. **Sign XML Document:** Use the GUI to select an input XML file and specify an output file path. Click the "Sign XML" button to sign the document.
3. **Verify XML Document:** Use the GUI to select a signed XML file. Click the "Verify XML" button to verify the document's signature.

## Dependencies

Ensure the following dependencies are included in your project:

### Maven Dependencies
Add the following dependencies to your `pom.xml` if you are using Maven:
'''xml
<dependencies>
    <dependency>
        <groupId>org.apache.santuario</groupId>
        <artifactId>xmlsec</artifactId>
        <version>2.3.0</version>
    </dependency>
</dependencies>'''

### Manual Downloads
If you are not using Maven, download the Apache XML Security for Java library from Apache XML Security and include it in your project.

### Java Version
Ensure you are using Java 8 or higher.

### Working
## Signing XML Document:
- Run the Main class to launch the GUI.
- Select the input XML file and specify the output file path.
- Click the "Sign XML" button to sign the document using a generated key pair.
The signed XML document will be saved at the specified output file path.
## Verifying XML Document:
- Run the Main class to launch the GUI.
- Select the signed XML file.
- Click the "Verify XML" button to verify the document's signature using the extracted public key.
- The result of the verification will be displayed in a dialog box.
