package com.example.mybookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class BookCover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cover);
        File ReadingBook = (File)getIntent().getExtras().get("BookFile");
        //
        String resAuthor="-1";
        String resBookTitle = "-1";
        String resAnnotation = "-1";
        //Записываем все что находится в теге description
        byte[] desription = Fb2Book.findTag(ReadingBook,"description");
        //Записываем все что находится в теге author
        byte[] author = Fb2Book.findTag(desription,"author");
        //Записываем first-name автора
        byte[] authorFirst = Fb2Book.findTag(author,"first-name");
        //Записываем last-name автора
        byte[] authorLast = Fb2Book.findTag(author,"last-name");
        //Записываем book-title
        byte[] bookTitleBytes = Fb2Book.findTag(desription,"book-title");
        //Записываем анотацию
        byte[] annotationBytes = Fb2Book.findTag(desription,"annotation");
        //ImageBytes
        byte[]ImageBytes = Fb2Book.findImageID(ReadingBook,"cover.jpg");
        try {
            String FirstName = new String(authorFirst,"UTF-8");
            String LastName = new String(authorLast,"UTF-8");
            resAuthor = FirstName+" "+LastName;
            resBookTitle = new String(bookTitleBytes,"UTF-8");
            resAnnotation = new String(annotationBytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TextView authorView = findViewById(R.id.textView2);
        authorView.setText("Автор - "+resAuthor);
        TextView titleView = findViewById(R.id.textView3);
        titleView.setText(resBookTitle);
        TextView annotationView = findViewById(R.id.textView4);
        resAnnotation = resAnnotation.replaceAll("<p>","    ");
        resAnnotation = resAnnotation.replaceAll("</p>","");
        annotationView.setText(resAnnotation);
        //TextView text = findViewById(R.id.textView5);
        //String g = Base64.encodeToString(ImageBytes,0);
        //byte[] decode = Base64.decode(g,Base64.DEFAULT);
        //Bitmap decodeImage = BitmapFactory.decodeByteArray(decode,0,decode.length);
        //text.setText(g);
        ImageView imageView = findViewById(R.id.cover);
        byte[] res = Base64.decode(ImageBytes,1);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(res);
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//        if (bmp==null)
//        {
//            annotationView.setText("null");
//        }
        imageView.setImageBitmap(bmp);



        //
    }
    public void callread(View view)
    {
        File ReadingBook = (File)getIntent().getExtras().get("BookFile");

        Intent intent = new Intent(BookCover.this,ReedBook.class);
        intent.putExtra("BookFile",ReadingBook);
        startActivity(intent);
    }
}