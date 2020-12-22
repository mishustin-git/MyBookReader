package com.example.mybookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.mybookreader.R.id.booklist;
import static com.example.mybookreader.R.id.forever;

public class FindBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);
    }

    public void callBookCover(View view)
    {

    }

    public void myfunction(View view)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {

            //корневая дериктория
            File dir0 = android.os.Environment.getExternalStorageDirectory();
            //список дерикторий и файлов расположенных в коневой дериктории
            File[] dirs = dir0.listFiles();
            //File[] dirs = dir0.listFiles();

            //приведение статического массива к динамическому
            ArrayList<File> enotherDir = new ArrayList<>();
            for(int i=0;i<dirs.length;i++)
            {
                enotherDir.add(dirs[i]);
            }

            //Все найденные файлы
            final ArrayList<File> filesAll = myfunc1(enotherDir,2);

            //Создаем промежуточный список
            ArrayList<File> books_prev =  new ArrayList<>();
            for (File elem:filesAll) {
                String strElem = elem.getName();
                if (strElem.contains(".fb2") || strElem.contains(".txt")) {
                    books_prev.add(elem);
                }
            }
            //Перезаписываем константой
            final ArrayList<File> books = books_prev;


            //Получаем элемент списка
            ListView example = findViewById(R.id.booklist);
            //Получаем данные для списка
            List<String> booksList = new ArrayList<>();
            for (File elem:books) {
                    booksList.add(elem.getName());
            }
            //Создаем адаптер
            ArrayAdapter<String> adapterBooks = new ArrayAdapter(this,android.R.layout.simple_list_item_1,booksList);
            //Устанавливаем для списка адаптер
            example.setAdapter(adapterBooks);


            //Кликабельность списка меню
            //Ранее вызывался класс ReedBook
            /*AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(FindBooks.this,ReedBook.class);
                    intent.putExtra("BookFile",(File) books.get(i));
                    startActivity(intent);
                }
            };*/
            //Теперь вызывается BookCover, а далее уже вызовется и ReedBook
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(FindBooks.this,BookCover.class);
                    intent.putExtra("BookFile",(File) books.get(i));
                    startActivity(intent);
                }
            };
            example.setOnItemClickListener(itemClickListener);
            // example.setOnItemClickListener(itemClickListener);

        }
        else
            //запрос у пользователя на работу с дерикторией телефона
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }


    public ArrayList<File> myfunc1(ArrayList<File> dir,int p)
    {
        //p - шаг рекурсии
        ArrayList<File> dirs = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        for (File elem:dir) {
            if (elem.isFile())
            {
                files.add(elem);
            }
            else if (elem.isDirectory())
            {
                File[] temp = elem.listFiles();
                for(int i=0;i<temp.length;i++)
                {
                    dirs.add(temp[i]);
                }
            }
        }
        if (dirs != null && p !=0) {
            ArrayList<File> returnedFiles = myfunc1(dirs,p-1);
            for (File elem:returnedFiles) {
                files.add(elem);
            }
        }
        return files;
    }
}