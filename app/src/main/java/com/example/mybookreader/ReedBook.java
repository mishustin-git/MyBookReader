package com.example.mybookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

public class ReedBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reed_book);

        //получаем данные(файл читаемой книги) из главной активности
        File ReadingBook = (File)getIntent().getExtras().get("BookFile");
        //Создаем адаптер
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        //передаем адаптеру файл книги
        pagerAdapter.getFile(ReadingBook);
        //создаем разбиение текста на страницы
        pagerAdapter.getStrings(1000);
        //
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
    }
    private class SectionPagerAdapter extends FragmentPagerAdapter{

        private String str;

        private List<String> strings;

        public void getFile(File ReadingBook)
        {
            str = Fb2Book.getCodeBook(ReadingBook);
        }

        public void getStrings(int size)
        {
            strings = Fb2Book.getStrings(str,size);
        }

        public SectionPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public int getCount(){

            return 4;
            //
        }

        @Override
        public Fragment getItem(int position){
            //
            return new BlankFragment(strings.get(position));
        }
    }
}