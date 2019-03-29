package com.example.europeesaanrijdingsformulier.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.util.Base64
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.report.Report
import com.itextpdf.text.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.*


class PdfWriterManager {


    fun writePDF(report: Report, activity: FragmentActivity?): File {

        val pdfFolder = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ), "pdfdemo"
        )
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs()
        }
        val file = File(pdfFolder, "aanrijdingsformulier.pdf")

        val streamPDF = ByteArrayOutputStream()
        var document = Document(PageSize.A4, 0F, 0F, 0F, 0F)
        val writer = PdfWriter.getInstance(document, FileOutputStream(file))
        document.open()
        val canvas: PdfContentByte
        canvas = writer.directContentUnder

        val d = activity!!.getResources().getDrawable(R.drawable.aanrijdingsform)
        val bitDw = (d as BitmapDrawable)
        val bmp = bitDw.getBitmap()
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.scaleAbsolute(595F, 842F)

        image.setAbsolutePosition(0F, 0F)
        canvas.addImage(image)
        //Aanrijdingsgegevens
        setPara(
            writer.getDirectContent(),
            Phrase(report.dateCrash!!.date.toString()+"/"+(report.dateCrash!!.month+1).toString()+"/"+(report.dateCrash!!.year+1900).toString(), Font(Font.FontFamily.COURIER, 9F)),
            0.40F,
            0.91F
        )
        setPara(
            writer.getDirectContent(),
            Phrase((report.dateCrash!!.hours).toString()+":"+report.dateCrash.minutes.toString(), Font(Font.FontFamily.COURIER, 9F)),
            2.69F,
            0.87F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.country, Font(Font.FontFamily.COURIER, 6F)),
            4.21F,
            0.87F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.postalCode+" "+report.city, Font(Font.FontFamily.COURIER, 9F)),
            6.01F,
            0.55F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.street+" "+report.streetNumber, Font(Font.FontFamily.COURIER, 9F)),
            5.38F,
            0.87F
        )
        //verzekeringsnemer A
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().lastName, Font(Font.FontFamily.COURIER, 9F)),
            1.10F,
            3.07F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().email, Font(Font.FontFamily.COURIER, 6F)),
            1.61F,
            4.34F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().firstName, Font(Font.FontFamily.COURIER, 9F)),
            1.38F,
            3.37F
        )

        //voertuig A
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().brand+" "+report.profiles.first().vehicles!!.first().model, Font(Font.FontFamily.COURIER, 6F)),
            0.44F,
            5.50F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().licensePlate, Font(Font.FontFamily.COURIER, 9F)),
            0.44F,
            6.03F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().country, Font(Font.FontFamily.COURIER, 9F)),
            0.44F,
            6.58F
        )
        //verzekering A
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().insurance!!.insurer!!.name, Font(Font.FontFamily.COURIER, 9F)),
            1.10F,
            7.28F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().insurance!!.insuranceNumber, Font(Font.FontFamily.COURIER, 9F)),
            1.46F,
            7.56F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().insurance!!.greenCardNumber, Font(Font.FontFamily.COURIER, 9F)),
            2.22F,
            7.87F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().insurance!!.expires!!.date.toString()+"/"+(report.profiles.first().vehicles!!.first().insurance!!.expires!!.month+1).toString()+"/"+(report.profiles.first().vehicles!!.first().insurance!!.expires!!.year+1900).toString(), Font(Font.FontFamily.COURIER, 6F)),
            4.13F,
            8.38F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().vehicles!!.first().insurance!!.emailAgency, Font(Font.FontFamily.COURIER, 6F)),
            1.63F,
            10F
        )
        //Bestuurder A
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().lastName, Font(Font.FontFamily.COURIER, 9F)),
            1.23F,
            11.16F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().firstName, Font(Font.FontFamily.COURIER, 9F)),
            1.42F,
            11.47F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().email, Font(Font.FontFamily.COURIER, 6F)),
            1.67F,
            12.69F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().license?.licenseNumber, Font(Font.FontFamily.COURIER, 9F)),
            1.55F,
            12.98F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().license?.category, Font(Font.FontFamily.COURIER, 9F)),
            2.18F,
            13.27F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.first().license?.expires.toString(), Font(Font.FontFamily.COURIER, 9F)),
            2.14F,
            13.57F
        )

        //verzekeringsnemer B
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().lastName, Font(Font.FontFamily.COURIER, 9F)),
            9.80F,
            3.07F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().email, Font(Font.FontFamily.COURIER, 6F)),
            10.35F,
            4.34F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().firstName, Font(Font.FontFamily.COURIER, 9F)),
            10.12F,
            3.37F
        )

        //voertuig B
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().brand+" "+report.profiles.last().vehicles!!.first().model, Font(Font.FontFamily.COURIER, 6F)),
            9.14F,
            5.50F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().licensePlate, Font(Font.FontFamily.COURIER, 9F)),
            9.14F,
            6.03F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().country, Font(Font.FontFamily.COURIER, 9F)),
            9.10F,
            6.58F
        )
        //verzekering B
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().insurance!!.insurer!!.name, Font(Font.FontFamily.COURIER, 9F)),
            9.78F,
            7.28F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().insurance!!.insuranceNumber, Font(Font.FontFamily.COURIER, 9F)),
            10.14F,
            7.56F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().insurance!!.greenCardNumber, Font(Font.FontFamily.COURIER, 9F)),
            10.92F,
            7.87F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().insurance!!.expires!!.date.toString()+"/"+(report.profiles.last().vehicles!!.first().insurance!!.expires!!.month+1).toString()+"/"+(report.profiles.last().vehicles!!.first().insurance!!.expires!!.year+1900).toString(), Font(Font.FontFamily.COURIER, 6F)),
            12.83F,
            8.38F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().vehicles!!.first().insurance!!.emailAgency, Font(Font.FontFamily.COURIER, 6F)),
            10.31F,
            10F
        )
        //Bestuurder B
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().lastName, Font(Font.FontFamily.COURIER, 9F)),
            9.80F,
            11.16F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().firstName, Font(Font.FontFamily.COURIER, 9F)),
            10.08F,
            11.47F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().email, Font(Font.FontFamily.COURIER, 6F)),
            10.33F,
            12.69F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().license?.licenseNumber, Font(Font.FontFamily.COURIER, 9F)),
            10.20F,
            12.98F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().license?.category, Font(Font.FontFamily.COURIER, 9F)),
            10.86F,
            13.27F
        )
        setPara(
            writer.getDirectContent(),
            Phrase(report.profiles.last().license?.expires.toString(), Font(Font.FontFamily.COURIER, 9F)),
            10.82F,
            13.57F
        )

/*

        PDFToImage.main()

        val targetstream = ByteArrayInputStream(streamPDF.toByteArray())
        val baos = ByteArrayOutputStream()
        val pddoc:PDDocument
        pddoc = PDDocument.load(targetstream)
        val renderer = PDFRenderer(pddoc)
        val bi = renderer.renderImageWithDPI(1,300F)

        val reader = PdfReader(stream.toByteArray())
        val page = writer.getImportedPage(reader,1)

*/




        document.close()

        return file//Base64.encodeToString(file.readBytes(),Base64.NO_WRAP)

    }


    fun setPara(canvas: PdfContentByte, p: Phrase, x: Float, y: Float) {
        var x = x * 42.719F
        var y = 842 - (y * 42.775F)

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, p, x, y, 0f)
    }
}
