package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Word1 {

    String classname;
    String[][] b;

    public Word1() {
    }

    public Word1(String classname, String[][] b) {
        this.b = b;
        this.classname = classname;
    }

    public void createWordFile() throws SQLException, ClassNotFoundException {
        DataAccesor dataaccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");

        ClassDetail detail = dataaccessor.getSingle(classname);

        int collum = detail.collumn;
        int row = (detail.strength % collum == 0) ? detail.strength / collum : (1 + (detail.strength / collum));

        System.out.print(b);
        System.out.print("printing function is being called");
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph para = doc.createParagraph();
        XWPFRun run = para.createRun();

        run.setBold(true);
        run.setText("NIT RAIPUR");
        run.addBreak();
        para.setAlignment(ParagraphAlignment.CENTER);
        para.setBorderBottom(Borders.BASIC_THIN_LINES);
        //para2.setBorderTop(Borders.BASIC_THIN_LINES);

        XWPFParagraph para3 = doc.createParagraph();
        para3.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run3 = para.createRun();
        run3.setBold(true);
        run3.setText("Seating Arrangment for End sem Exam");
        run3.addBreak();

        XWPFParagraph para2 = doc.createParagraph();
        para2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run2 = para.createRun();
        run2.setBold(true);
        run2.setText(" Class room :" + classname);

        XWPFTable table = doc.createTable();

        XWPFTableRow tableRowOne = table.getRow(0);
        // tableRowOne.getCell(0).setText("col one");
        for (int k = 0; k < collum; k++) {
            tableRowOne.addNewTableCell().setText("col" + (k + 1));
        }
        //create second row
        for (int j = 1; j <= row + 1; j++) {
            XWPFTableRow tableRowTwo = table.createRow();
            for (int i = 0; i <= collum; i++) {
                tableRowTwo.getCell(i).setText(b[j - 1][i]);
            }
        }

        try {
            //FileOutputStream output = new FileOutputStream("WordBna.docx");

            FileOutputStream output = new FileOutputStream("C:\\Users\\HP1\\Desktop\\" + classname + ".docx");
            doc.write(output);
            System.out.print("table created");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createAttendanceDocx(String name) throws SQLException, ClassNotFoundException {
        DataAccesor dataaccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");

        ArrayList<ClassBranch> list = dataaccessor.getList(name);
        Collections.sort(list, new Comparator<ClassBranch>() {
            @Override
            public int compare(ClassBranch lhs, ClassBranch rhs) {
                int x = lhs.end - lhs.start;
                int y = rhs.end - rhs.start;
                return (x > y) ? 1 : 0;
            }
        });

        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph para = doc.createParagraph();
        XWPFRun run = para.createRun();

        run.setBold(true);
        run.setText("NIT RAIPUR");
        run.addBreak();
        para.setAlignment(ParagraphAlignment.CENTER);
        para.setBorderBottom(Borders.BASIC_THIN_LINES);
        //para2.setBorderTop(Borders.BASIC_THIN_LINES);

        XWPFParagraph para3 = doc.createParagraph();
        para3.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run3 = para.createRun();
        run3.setBold(true);
        run3.setText("Seating Arrangment for End sem Exam");
        run3.addBreak();

        XWPFParagraph para2 = doc.createParagraph();
        para2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run2 = para.createRun();
        run2.setBold(true);
        run2.setText(" Class room :" + name);

        XWPFTable table = doc.createTable();
        XWPFTableRow tableRowOne = table.getRow(0);
        for (int k = 0; k < 2 * list.size(); k++) {
            if (k % 2 == 0)
                tableRowOne.addNewTableCell().setText("   Roll No   ");
            else {
                tableRowOne.addNewTableCell().setText("  Signature  ");
            }
        }
        ClassBranch x=null;
        x= list.get(0);
        String detail = x.branch + "(" + x.sem + ")";
        int row = x.end - x.start + 1;
        int j=0;
        int rollNo = x.start;

        for (j = 0; j < row; j++) {
            try {
                XWPFTableRow tableRow = table.createRow();
                tableRow.getCell(1).setText(detail + "-" + rollNo);
                tableRow.getCell(2).setText("        ");
                rollNo++;
            }catch (Exception e){
                System.out.println("Exception in Word1/createAttendanceDocx()/for loop00"+e);
            }
        }
        if(x.sliderStart!=0 && x.sliderEnd!=0){
            row = 1+x.sliderEnd-x.sliderStart+j;
            rollNo = x.sliderStart;
            for ( ; j <= row; j++) {
                try {
                    XWPFTableRow tableRow = table.getRow(j);
                    tableRow.getCell(1).setText(detail + "-" + rollNo);
                    tableRow.getCell(2).setText("        ");
                    rollNo++;
                } catch (Exception e) {
                    System.out.println("Exception in Word1/createAttendanceDocx()/for loop22"+e);
                }
            }
        }
       for (int i = 1; i < 2 * list.size() - 1; i += 2) {
            x=list.get((i / 2) + 1);
            row = x.end - x.start + 1;
            detail = x.branch + "(" + x.sem + ")";
            rollNo = x.start;
            for (j = 1; j <= row; j++) {
                try {
                    XWPFTableRow tableRow = table.getRow(j);
                    tableRow.getCell(i + 2).setText(detail + "-" + rollNo);
                    tableRow.getCell(i + 3).setText("        ");
                    rollNo++;
                } catch (Exception e) {
                    System.out.println("Exception in Word1/createAttendanceDocx()/for loop1"+e);
                }
            }
            if(x.sliderStart!=0 && x.sliderEnd!=0){
                row = 1+x.sliderEnd-x.sliderStart+j;
                rollNo = x.sliderStart;
                for ( ; j <= row; j++) {
                    try {
                        XWPFTableRow tableRow = table.getRow(j);
                        tableRow.getCell(i + 2).setText(detail + "-" + rollNo);
                        tableRow.getCell(i + 3).setText("        ");
                        rollNo++;
                    } catch (Exception e) {
                        System.out.println("Exception in Word1/createAttendanceDocx()/for loop2"+e);
                    }
                }
            }
        }

        try {
            FileOutputStream output = new FileOutputStream("C:\\Users\\HP1\\Desktop\\" + name + "Attendance.docx");
            doc.write(output);
            System.out.print("table created");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createPDFSeating() throws SQLException, ClassNotFoundException {
        System.out.printf("CreatePDF called ");

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

        DataAccesor dataaccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");

        ClassDetail detail = dataaccessor.getSingle(classname);

        int collum = detail.collumn;
        int row = (detail.strength % collum == 0) ? (detail.strength / collum) : (1 + (detail.strength / collum));

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\HP1\\Desktop\\" + classname + ".pdf"));
            document.open();

            Anchor anchor = new Anchor("");

            Paragraph para1 = new Paragraph("NIT RAIPUR", catFont);
            para1.setAlignment(Element.ALIGN_CENTER);
            document.add(para1);

            Paragraph para2 = new Paragraph("END SEMESTER EXAMINATION "+" 2016", catFont);
            para2.setAlignment(Element.ALIGN_CENTER);
            document.add(para2);

            Paragraph para3 = new Paragraph("CLASS ROOM : "+"SN-3", subFont);
            para3.setAlignment(Element.ALIGN_CENTER);
            document.add(para3);

            Paragraph para4 = new Paragraph("Seating Arrangement :");

            PdfPTable table = new PdfPTable(collum);

            table.setWidthPercentage(110);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell c1 = new PdfPCell(new Phrase("column 1"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);

            for(int i=2;i<=collum;i++) {
                c1 = new PdfPCell(new Phrase("column "+i));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(c1);
            }
            table.setHeaderRows(1);

            for (int j = 1; j <= row; j++) {
                for (int i = 1; i <= collum; i++) {
                    table.addCell(b[j][i]);
                }
            }
            para4.add(table);
            document.add(para4);

            Paragraph para5 = new Paragraph("Class room details :");
            para5.setSpacingBefore(50f);
            para5.setAlignment(Element.ALIGN_LEFT);

            PdfPTable table1 = new PdfPTable(3);

            table1.setWidthPercentage(90);
            table1.setSpacingAfter(10f);

            PdfPCell c2 = new PdfPCell(new Phrase("BRANCH and SEMSTER"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1.addCell(c2);

            c2 = new PdfPCell(new Phrase("Roll Numbers"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1.addCell(c2);

            c2 = new PdfPCell(new Phrase("Total"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table1.addCell(c2);

            table1.setHeaderRows(1);
            ClassBranch x;
            ArrayList<ClassBranch> list = dataaccessor.getList(classname);
            int n = list.size(),total=0;
            String rollRange="";
            for(int i=0;i<n;i++){
                x=list.get(i);
                if(x.sliderStart==0) {
                    total = (x.end-x.start+1);
                    rollRange = x.start+" to "+x.end;
                }
                else {
                    total = (x.end-x.start+2+x.sliderEnd-x.sliderStart);
                    rollRange = x.start+" to "+x.end +" and "+x.sliderStart+" to "+x.sliderEnd;
                }

                c2 = new PdfPCell(new Phrase(x.branch+"(" +x.sem+"sem )"));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(rollRange));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(c2);

                c2 = new PdfPCell(new Phrase(" "+total));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(c2);

            }
            para5.add(table1);
            document.add(para5);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pdfAttendence(String name) throws SQLException, ClassNotFoundException {

        DataAccesor dataaccessor = new DataAccesor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vigyaan", "root", "");

        ArrayList<ClassBranch> list = dataaccessor.getList(name);
        Collections.sort(list, new Comparator<ClassBranch>() {
            @Override
            public int compare(ClassBranch lhs, ClassBranch rhs) {
                int x = lhs.end - lhs.start;
                int y = rhs.end - rhs.start;
                return (x > y) ? 1 : 0;
            }
        });

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\HP1\\Desktop\\" + classname + "Attendance.pdf"));
            document.open();

            Anchor anchor = new Anchor("");

            Paragraph para1 = new Paragraph("NATIONAL INSTITUTE OF TECHNOLOGY RAIPUR\n(INSTITUTE OF NATIONAL IMPORTANTANCE)", catFont);
            para1.setAlignment(Element.ALIGN_CENTER);
            document.add(para1);

            Paragraph para2 = new Paragraph("END SEMESTER EXAMINATION "+" 2016", catFont);
            para2.setAlignment(Element.ALIGN_CENTER);
            document.add(para2);

            Paragraph para3 = new Paragraph("CLASS ROOM : "+"SN-3", subFont);
            para3.setAlignment(Element.ALIGN_CENTER);
            document.add(para3);

            Paragraph para4 = new Paragraph("---------------------------------------" +
                    "----------------------------------------------------------------" +
                    "-----------------------"+"\nSeating Arrangement :");
            para4.setAlignment(Element.ALIGN_LEFT);


            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell c1 = new PdfPCell(new Phrase("Roll Number"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);

            c1=new PdfPCell(new Phrase("Answer Sheet No."));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);

            c1=new PdfPCell(new Phrase("Signature"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);
            table.setHeaderRows(1);

            int n,k=0,rollNo=0;
            ClassBranch x;
            while(k<list.size()) {
                x=list.get(k);
                //n = x.end-x.start+1;
                //rollNo=x.start;
                for (int i = x.start; i <= x.end; i++) {
                    c1 = new PdfPCell(new Phrase(x.branch+"(" +x.sem+")-"+ i));
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(c1);

                    c1 = new PdfPCell(new Phrase(""));
                    table.addCell(c1);
                    table.addCell(c1);
                    //rollNo++;
                }
                if(x.sliderStart!=0 && x.sliderEnd!=0){
                    for (int i = x.sliderStart; i <= x.sliderEnd; i++) {
                        c1 = new PdfPCell(new Phrase(x.branch+"(" +x.sem+")-"+ i));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(c1);

                        c1 = new PdfPCell(new Phrase(""));
                        table.addCell(c1);
                        table.addCell(c1);
                        //rollNo++;
                    }
                }
                k++;
            }
            para4.add(table);
            document.add(para4);

            Paragraph para5 = new Paragraph("Class room details :");
            para5.setSpacingBefore(50f);
            para5.setAlignment(Element.ALIGN_LEFT);

            PdfPTable table2 = new PdfPTable(4);

            table2.setWidthPercentage(90);
            table2.setSpacingAfter(10f);

            PdfPCell c2 = new PdfPCell(new Phrase("BRANCH and SEMSTER"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2.addCell(c2);

            c2 = new PdfPCell(new Phrase("Total"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2.addCell(c2);

            c2 = new PdfPCell(new Phrase("NO.of Present Student"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2.addCell(c2);

            c2 = new PdfPCell(new Phrase("Roll No.of Absent Student"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table2.addCell(c2);

            table2.setHeaderRows(1);
            n=list.size();
            int total=0;
            for(int i=0;i<n;i++){

                x=list.get(i);
                if(x.sliderStart==0) total = (x.end-x.start+1);
                else total = (x.end-x.start+2+x.sliderEnd-x.sliderStart);

                c2 = new PdfPCell(new Phrase(x.branch+"(" +x.sem+"sem )"));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table2.addCell(c2);

                c2 = new PdfPCell(new Phrase(""+total ));       // changes here
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table2.addCell(c2);

                c2 = new PdfPCell(new Phrase(" "));
                table2.addCell(c2);
                table2.addCell(c2);
            }
            para5.add(table2);
            document.add(para5);

            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph para6 = new Paragraph("Date");
            para6.setSpacingBefore(25f);
            para6.add(new Chunk(glue));
            para6.add("Signature Invigilator");

            document.add(para6);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
















