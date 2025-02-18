package XMLsignature.XMLsignature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.KeyPair;
import java.security.PublicKey;

public class Main {
    private JFrame frame;
    private JTextField inputFilePathField;
    private JTextField outputFilePathField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblInputFile = new JLabel("Input File:");
        lblInputFile.setBounds(10, 11, 80, 14);
        frame.getContentPane().add(lblInputFile);

        inputFilePathField = new JTextField();
        inputFilePathField.setBounds(100, 8, 250, 20);
        frame.getContentPane().add(inputFilePathField);
        inputFilePathField.setColumns(10);

        JButton btnBrowseInput = new JButton("Browse");
        btnBrowseInput.setBounds(360, 7, 80, 23);
        btnBrowseInput.addActionListener(e -> chooseFile(inputFilePathField));
        frame.getContentPane().add(btnBrowseInput);

        JLabel lblOutputFile = new JLabel("Output File:");
        lblOutputFile.setBounds(10, 42, 80, 14);
        frame.getContentPane().add(lblOutputFile);

        outputFilePathField = new JTextField();
        outputFilePathField.setBounds(100, 39, 250, 20);
        frame.getContentPane().add(outputFilePathField);
        outputFilePathField.setColumns(10);

        JButton btnBrowseOutput = new JButton("Browse");
        btnBrowseOutput.setBounds(360, 38, 80, 23);
        btnBrowseOutput.addActionListener(e -> chooseFile(outputFilePathField));
        frame.getContentPane().add(btnBrowseOutput);

        JButton btnSign = new JButton("Sign XML");
        btnSign.setBounds(100, 100, 120, 23);
        btnSign.addActionListener(new SignButtonActionListener());
        frame.getContentPane().add(btnSign);

        JButton btnVerify = new JButton("Verify XML");
        btnVerify.setBounds(230, 100, 120, 23);
        btnVerify.addActionListener(new VerifyButtonActionListener());
        frame.getContentPane().add(btnVerify);
    }

    private void chooseFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private class SignButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String inputFilePath = inputFilePathField.getText();
                String outputFilePath = outputFilePathField.getText();
                if (inputFilePath.isEmpty() || outputFilePath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select both input and output files.");
                    return;
                }

                KeyPair keyPair = XMLSigner.generateKeys();
                XMLSigner.signXML(inputFilePath, outputFilePath, keyPair);
                JOptionPane.showMessageDialog(frame, "XML document signed successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while signing the XML document: " + ex.getMessage());
            }
        }
    }

    private class VerifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String inputFilePath = inputFilePathField.getText();
                if (inputFilePath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select an input file.");
                    return;
                }

                PublicKey publicKey = XMLVerifier.extractPublicKey(inputFilePath);
                boolean isValid = XMLVerifier.verifyXMLSignature(inputFilePath, publicKey);
                if (isValid) {
                    JOptionPane.showMessageDialog(frame, "XML signature is valid.");
                } else {
                    JOptionPane.showMessageDialog(frame, "XML signature is invalid.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while verifying the XML document: " + ex.getMessage());
            }
        }
    }
}