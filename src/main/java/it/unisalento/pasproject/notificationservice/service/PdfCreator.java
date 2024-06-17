package it.unisalento.pasproject.notificationservice.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import it.unisalento.pasproject.notificationservice.exceptions.PdfCreationException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfCreator {
    public ByteArrayOutputStream createPdf(String content) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);

            document.open();
            document.add(new Paragraph(content));
            document.close();

        } catch (DocumentException e) {
            throw new PdfCreationException("Error creating PDF: " + e.getMessage());
        }

        return outputStream;
    }
}
