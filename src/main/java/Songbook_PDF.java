import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import java.io.IOException;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
//import static org.eclipse.lsp4j.UniquenessLevel.Document;

//klasa dostarcza metod obsługujących pliki PDF, korzystając z biblioteki itext7
public class Songbook_PDF {
    //statyczna metoda generuje śpiewnik w formacie PDF, dodaje okładkę w zależności od wartości "importCoverImage"
    //generuje spis treści oraz właściwą część śpiewnika z tekstami i chwytami piosenek
    public static void GeneratePDF(Songbook songbook, String DEST, String coverImage, Boolean importCoverImage) throws IOException{
        int songsNumber = songbook.titles.size();
        for(int i=0; i<songsNumber; i++){
            for(int j = i+1; j<songsNumber; j++){
                if(songbook.songs.get(i).text.equals(songbook.songs.get(j).text)){
                    songbook.songs.remove(j);
                }
            }
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc, PageSize.A4);
        doc.setLeftMargin(60);
        doc.setRightMargin(60);
        PdfFont font = PdfFontFactory.createFont(HELVETICA, PdfEncodings.CP1250);
        doc.setFont(font);

        //obliczanie pozycji i dodanie wyśrodkowanego obrazu lub tytułu śpiewnika

        float documentWidth = doc.getPdfDocument().getDefaultPageSize().getWidth();
        float documentHeight = doc.getPdfDocument().getDefaultPageSize().getHeight();
        float pageWidth = PageSize.A4.getWidth();
        float pageHeight = PageSize.A4.getHeight();

        if(importCoverImage) {
            Image image = new Image(ImageDataFactory.create(coverImage));

            float imageWidth = image.getImageScaledWidth();
            float imageHeight = image.getImageScaledHeight();
            float xPos = (documentWidth - imageWidth) / 2;
            float yPos = (documentHeight - imageHeight) / 2;
            image.setFixedPosition(xPos, yPos);
            doc.add(image);
        }
        else{
            Paragraph paragraph = new Paragraph(songbook.title)
                    .setFontSize(36)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setWidth(UnitValue.createPointValue(PageSize.A4.getWidth() - doc.getLeftMargin() - doc.getRightMargin()))
                    .setHeight(UnitValue.createPointValue(100));

            float paragraphWidth = paragraph.getWidth().getValue();
            float paragraphHeight = paragraph.getHeight().getValue();
            float x = (pageWidth - paragraphWidth) / 2;
            float y = (pageHeight - paragraphHeight) / 2;

            paragraph.setFixedPosition(x, y, paragraphWidth);
            doc.add(paragraph);
        }

        //ustawienie prezentacji tekstu w dwóch kolumnach

        float columnWidth = PageSize.A4.getWidth() / 2 - doc.getLeftMargin() - doc.getRightMargin();
        float columnHeight = PageSize.A4.getHeight() - doc.getTopMargin() - doc.getBottomMargin();
        Rectangle[] columns = {new Rectangle(doc.getLeftMargin(), doc.getBottomMargin(), columnWidth, columnHeight),
                new Rectangle(documentWidth - doc.getLeftMargin() - columnWidth, doc.getBottomMargin(), columnWidth, columnHeight)};
        ColumnDocumentRenderer columnRenderer = new ColumnDocumentRenderer(doc, columns);
        doc.setRenderer(columnRenderer);

        //tworzenie spisu treści

        List list = new List(ListNumberingType.DECIMAL)
                .setSymbolIndent(12)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        for (int i = 1; i <= songsNumber; i++) {
            ListItem item = new ListItem(songbook.titles.get(i-1));
            list.add(item);
        }
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        doc.add(list);

        //generowanie właściwej części śpiewnika

        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        for (int i = 1; i <= songsNumber; i++) {
            Paragraph title = new Paragraph(i + ". " + songbook.titles.get(i-1)).setBold();
            Paragraph chords = new Paragraph(songbook.songs.get(i-1).chords);
            Paragraph text = new Paragraph(songbook.songs.get(i-1).text);
            doc.add(title);
            doc.add(chords);
            doc.add(text);
        }

        doc.close();
        pdfDoc.close();
    }
    //statyczna metoda sortuje plik PDF znajdujący się pod ścieżką SRC do druku w formacie książeczki A5
    //i zapisuje go pod adresem DEST
    public static void PagePrintSort(String SRC, String DEST) throws IOException {
        PdfDocument sourcePdf = new PdfDocument(new PdfReader(SRC));
        PdfDocument doc = new PdfDocument(new PdfWriter(DEST));
        PdfMerger merger = new PdfMerger(doc);
        PageSize ps = new PageSize(sourcePdf.getFirstPage().getPageSize());
        int n = sourcePdf.getNumberOfPages();

        //sortuje strony w zaleźności od reszty z dzielenia ich liczby przez 4

        if(n%4 == 0 || n <=2){
            int m = n;
            for(int i = 1; i<=m/2; i++){
                if (i % 2 != 0) {
                    merger.merge(sourcePdf, m + 1 - i, m + 1- i);
                    merger.merge(sourcePdf, i, i);
                } else {
                    merger.merge(sourcePdf, i, i);
                    merger.merge(sourcePdf, m + 1 - i, m + 1 - i);
                }
            }
        }
        else if(n%4 == 1){
            int m = n+3;
            doc.addNewPage(1, ps);
            merger.merge(sourcePdf, 1, 2);
            doc.addNewPage(4, ps);
            doc.addNewPage(5, ps);
            merger.merge(sourcePdf, 3, 3);
            for(int i = 4; i<=m/2; i++){
                if (i % 2 != 0) {
                    merger.merge(sourcePdf, m + 1 - i, m + 1- i);
                    merger.merge(sourcePdf, i, i);
                } else {
                    merger.merge(sourcePdf, i, i);
                    merger.merge(sourcePdf, m + 1 - i, m + 1 - i);
                }
            }
        }
        else if(n%4 == 2){
            int m = n+2;
            doc.addNewPage(1, ps);
            merger.merge(sourcePdf, 1, 2);
            doc.addNewPage(4, ps);
            for(int i = 3; i<=m/2; i++){
                if (i % 2 != 0) {
                    merger.merge(sourcePdf, m + 1 - i, m + 1- i);
                    merger.merge(sourcePdf, i, i);
                } else {
                    merger.merge(sourcePdf, i, i);
                    merger.merge(sourcePdf, m + 1 - i, m + 1 - i);
                }
            }
        }
        else{
            int m = n+1;
            doc.addNewPage(1, ps);
            merger.merge(sourcePdf, 1, 1);
            for(int i = 2; i<=m/2; i++){
                if (i % 2 != 0) {
                    merger.merge(sourcePdf, m + 1 - i, m + 1- i);
                    merger.merge(sourcePdf, i, i);
                } else {
                    merger.merge(sourcePdf, i, i);
                    merger.merge(sourcePdf, m + 1 - i, m + 1 - i);
                }
            }
        }

        sourcePdf.close();
        doc.close();
    }
}
