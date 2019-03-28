package com.example.europeesaanrijdingsformulier.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.support.v4.app.FragmentActivity
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.report.Report
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.text.Phrase


class PdfWriterManager {


    fun writePDF(report: Report, activity : FragmentActivity?): String{

        val pdfFolder = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ), "pdfdemo"
        )
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs()
        }
        val file = File(pdfFolder, "aanrijdingsformulier.pdf")

        var document = Document(PageSize.A4,0F,0F,0F,0F)
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
        image.scaleAbsolute(595F,842F )

        image.setAbsolutePosition(0F,0F)
        canvas.addImage(image)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().lastName, Font(Font.FontFamily.COURIER,9F)), 1.23F,11.16F)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().firstName, Font(Font.FontFamily.COURIER,9F)), 1.42F,11.47F)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().email, Font(Font.FontFamily.COURIER,6F)), 1.67F,12.69F)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().license?.licenseNumber, Font(Font.FontFamily.COURIER,9F)), 1.55F,12.98F)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().license?.category, Font(Font.FontFamily.COURIER,9F)), 2.18F,13.27F)
        setPara(writer.getDirectContent(),  Phrase(report.profiles.first().license?.expires.toString(), Font(Font.FontFamily.COURIER,9F)), 2.14F,13.57F)

        document.close()

        return "test"

    }


    fun setPara(canvas: PdfContentByte, p: Phrase, x: Float, y: Float) {
       var x = x * 42.719F
        var y = 842 - (y * 42.775F)

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, p, x, y, 0f)
    }
}
