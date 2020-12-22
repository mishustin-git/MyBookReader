package com.example.mybookreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fb2Book {

    static public String getCodeBook(File ReadingBook){

        String res = "-1";


        /*
         *
         * Скопировано с ReedBook
         * На данный момент перекраиваю код
         *
         * */
        try {
            //отрываем файл в потоке
            //четсно говоря, не помню почему нужны именно следующие две строчки, но так работает
            //поэтому лучше не трогать
            //связанно с тем где расположен файл, с которым мы хотим работать
            FileInputStream inputStream = new FileInputStream(ReadingBook.getAbsoluteFile());
            BufferedReader inputStr = new BufferedReader(new InputStreamReader(inputStream));
            //определяем переменную считываемую информацию
            byte[] bytes = new byte[inputStream.available()];
            //считаываем информацию
            inputStream.read(bytes);
            //вычисляем количество считанных байт
            int length = bytes.length;
            //определяем указатель на конец текстового содержания книги .fb2
            int pointer = 1;
            //счетчик
            int i = 0;
            //Ищем в считанной информации "</body>"
            //Цикл проходит все байты
            //На выходе получаем изменненный pointer
            //Он указывает на конец тега </body>, именно на - >
            //Факт выше нужно учитывать далее
            while(i<length)
            {
                if (bytes[i]=='<' && bytes[i+1]=='/')
                {
                    char[] arrayChar = {(char)bytes[i],(char)bytes[i+1],(char)bytes[i+2],(char)bytes[i+3],(char)bytes[i+4],(char)bytes[i+5],(char)bytes[i+6]};
                    String tag = new String(arrayChar);
                    if (tag.equals("</body>"))
                    {
                        pointer=i+6;
                    }
                }
                i++;
            }
            //создаем новый массив, с известным количеством текстовой информации в книге .fb2
            byte[] newBytes = new byte[pointer+1];
            //переписываем из всей информации(bytes), только текстовую информацию
            //сначала и точно по pointer
            for (i = 0;i<=pointer;i++)
            {
                newBytes[i] = bytes[i];
            }
            //в переменную string res записываем массив байт newBytes, с кодировкой UTF-8
            //и все это при помощи конструктора класса String
            res = new String(newBytes,"UTF-8");
            //Тут и задавать кодировку, только суметь заранее определить
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    static public List<String> getStrings(String text, int size)
    {
        List<String> ret = new ArrayList<String>((text.length()+size-1)/size);
        for(int start = 0;start < text.length();start+=size)
        {
            ret.add(text.substring(start,Math.min(text.length(),start+size)));
        }
        return ret;
    }

    //Поиск тега
    //Параметрами являются:
    //файл книги, типа .fb2;и тег, без указания кавычек
    static public byte[] findTag(File ReadingBook,String tag)
    {
        String openTag = "<"+tag+">";
        String closeTag = "</"+tag+">";
        byte[] res = "-1".getBytes();
        try {
            //отрываем файл в потоке
            //четсно говоря, не помню почему нужны именно следующие две строчки, но так работает
            //поэтому лучше не трогать
            //связанно с тем где расположен файл, с которым мы хотим работать
            FileInputStream inputStream = new FileInputStream(ReadingBook.getAbsoluteFile());
            BufferedReader inputStr = new BufferedReader(new InputStreamReader(inputStream));
            //определяем переменную считываемую информацию
            byte[] bytes = new byte[inputStream.available()];
            //считаываем информацию
            inputStream.read(bytes);
            //вычисляем количество считанных байт
            int length = bytes.length;
            //счетчик
            int i = 0;

            /*------------------------------------------------*/
            //Ищем в считанной информации "</body>"
            //Цикл проходит все байты
            //На выходе получаем изменненный pointer
            //Он указывает на конец тега </body>, именно на - >
            //Факт выше нужно учитывать далее
            /*------------------------------------------------*/

            /*-----------------------------------------------------------------------------------------*/
            //Динамический массив байт
            ArrayList<Byte> arrayByte = new ArrayList<>();
            //Прохо по считанным
            int flag = 0;
            while(i<length)
            {
                //Запись содержимого не начата
                //Если найдено начало открывающего тега
                //Иначе если найден конец закрывающего тега
                //Иначе если flag!=0
                if (bytes[i]=='<' && bytes[i+1]!='/')
                {
                    //Длина тега полностью
                    int tagLenght = openTag.length();
                    //Массива символов длиной с открытый тег
                    char[] arrayChar = new char[tagLenght];
                    //Полная запись массива
                    for (int j= 0;j <tagLenght;j++)
                    {
                        arrayChar[j] = (char)bytes[i+j];
                    }
                    //Строка из записанного массива
                    String tagNow = new String(arrayChar);
                    //Если искомый тег(openTag) равен записанному из массива
                    if (tagNow.equals(openTag))
                    {
                        //и меняю flag на 1
                        flag = 1;
                        //начинаю запись
                    }
                }
                else if (bytes[i]=='<' && bytes[i+1]=='/') {
                    //Длина закрывающего тега
                    int tagLenght = closeTag.length();
                    //Массив символов длиной с закрытый тег
                    char[] arrayChar= new char[tagLenght];
                    //Полная запись массива
                    for(int j =0;j<tagLenght;j++)
                    {
                        arrayChar[j] = (char)bytes[i+j];
                    }
                    //Строка из записанного массива
                    String tagNow = new String(arrayChar);
                    //Если искомый тег(closeTag) равен записанному из массива
                    if (tagNow.equals(closeTag))
                    {
                        //Заканчиваю запись
                        //Возвращаю flag в 0
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1)
                {
                    arrayByte.add(bytes[i]);
                }
                i++;
            }
            int arrayLenght = arrayByte.toArray().length;
            for(i=0;i < openTag.length(); i++) {
                arrayByte.remove(0);
            }

            /*----------------------------------------------------------------------------------------------*/
            byte[] resultBytes = new byte[arrayLenght];
            i = 0;
            for (byte elem:arrayByte)
            {
                resultBytes[i] = elem;
                i++;
            }
            res = resultBytes;
            //res = new String(resultBytes,"UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    static public byte[] findTag(byte[] inputStr,String tag)
    {
        String openTag = "<"+tag+">";
        String closeTag = "</"+tag+">";
        byte[] res = "-1".getBytes();
            //отрываем файл в потоке
            //четсно говоря, не помню почему нужны именно следующие две строчки, но так работает
            //поэтому лучше не трогать
            //связанно с тем где расположен файл, с которым мы хотим работать
            //FileInputStream inputStream = new FileInputStream(ReadingBook.getAbsoluteFile());
            //BufferedReader inputStr = new BufferedReader(new InputStreamReader(inputStream));
            //определяем переменную считываемую информацию
            //byte[] bytes = new byte[inputStream.available()];
            byte[] bytes = inputStr;
            //считаываем информацию
            //inputStream.read(bytes);
            //вычисляем количество считанных байт
            int length = bytes.length;
            //счетчик
            int i = 0;

            /*------------------------------------------------*/
            //Ищем в считанной информации "</body>"
            //Цикл проходит все байты
            //На выходе получаем изменненный pointer
            //Он указывает на конец тега </body>, именно на - >
            //Факт выше нужно учитывать далее
            /*------------------------------------------------*/

            /*-----------------------------------------------------------------------------------------*/
            //Динамический массив байт
            ArrayList<Byte> arrayByte = new ArrayList<>();
            //Прохо по считанным
            int flag = 0;
            while(i<length)
            {
                //Запись содержимого не начата
                //Если найдено начало открывающего тега
                //Иначе если найден конец закрывающего тега
                //Иначе если flag!=0
                if (bytes[i]=='<' && bytes[i+1]!='/')
                {
                    //Длина тега полностью
                    int tagLenght = openTag.length();
                    //Массива символов длиной с открытый тег
                    char[] arrayChar = new char[tagLenght];
                    //Полная запись массива
                    for (int j= 0;j <tagLenght;j++)
                    {
                        arrayChar[j] = (char)bytes[i+j];
                    }
                    //Строка из записанного массива
                    String tagNow = new String(arrayChar);
                    //Если искомый тег(openTag) равен записанному из массива
                    if (tagNow.equals(openTag))
                    {
                        //и меняю flag на 1
                        flag = 1;
                        //начинаю запись
                    }
                }
                else if (bytes[i]=='<' && bytes[i+1]=='/') {
                    //Длина закрывающего тега
                    int tagLenght = closeTag.length();
                    //Массив символов длиной с закрытый тег
                    char[] arrayChar= new char[tagLenght];
                    //Полная запись массива
                    for(int j =0;j<tagLenght;j++)
                    {
                        arrayChar[j] = (char)bytes[i+j];
                    }
                    //Строка из записанного массива
                    String tagNow = new String(arrayChar);
                    //Если искомый тег(closeTag) равен записанному из массива
                    if (tagNow.equals(closeTag))
                    {
                        //Заканчиваю запись
                        //Возвращаю flag в 0
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1)
                {
                    arrayByte.add(bytes[i]);
                }
                i++;
            }
            int arrayLenght = arrayByte.toArray().length;
            if (arrayLenght>openTag.length())
                for(i=0;i < openTag.length(); i++) {
                    arrayByte.remove(0);
                }

            /*----------------------------------------------------------------------------------------------*/
            byte[] resultBytes = new byte[arrayLenght];
            i = 0;
            for (byte elem:arrayByte)
            {
                resultBytes[i] = elem;
                i++;
            }
            res = resultBytes;
            //res = new String(resultBytes,"UTF-8");
        return res;
    }

    static public byte[] findImageID(File ReadingBook,String id)
    {
        String openTag = "<binary";

        byte[] res = "-1".getBytes();

        ArrayList<Byte> ImageBytes = new ArrayList<>();
        try {
            //отрываем файл в потоке
            //четсно говоря, не помню почему нужны именно следующие две строчки, но так работает
            //поэтому лучше не трогать
            //связанно с тем где расположен файл, с которым мы хотим работать
            FileInputStream inputStream = new FileInputStream(ReadingBook.getAbsoluteFile());
            //BufferedReader inputStr = new BufferedReader(new InputStreamReader(inputStream));
            //определяем переменную считываемую информацию
            byte[] bytes = new byte[inputStream.available()];
            //считаываем информацию
            inputStream.read(bytes);
            //вычисляем количество считанных байт
            int length = bytes.length;
            //счетчик
            int i = 0;

            /*------------------------------------------------*/
            //Ищем в считанной информации "</body>"
            //Цикл проходит все байты
            //На выходе получаем изменненный pointer
            //Он указывает на конец тега </body>, именно на - >
            //Факт выше нужно учитывать далее
            /*------------------------------------------------*/

            /*-----------------------------------------------------------------------------------------*/
            //Динамический массив байт
            ArrayList<Byte> arrayByte = new ArrayList<>();
            //Прохо по считанным
            int flagAllBreak = 0;
            while(i<length)
            {
                //Запись содержимого не начата
                //Если найдено начало открывающего тега
                //Иначе если найден конец закрывающего тега
                //Иначе если flag!=0
                if (bytes[i]=='<' && bytes[i+1]!='/')
                {
                    //Длина тега полностью
                    int tagLenght = openTag.length();//openTag = "<binary"
                    //Массива символов длиной с открытый тег
                    char[] arrayChar = new char[tagLenght];
                    //Полная запись массива
                    for (int j= 0;j <tagLenght;j++)
                    {
                        arrayChar[j] = (char) bytes[i+j];
                    }
                    //Строка из записанного массива
                    String tagNow = new String(arrayChar);
                    //Если искомый тег(openTag) равен записанному из массива
                    if (tagNow.equals(openTag))
                    {
                        int j = tagLenght;
                        while(true)
                        {
                            //Поиск id
                            if (bytes[i+j]=='i' && bytes[i+j+1]=='d')
                            {
                                //Записываем id
                                ArrayList<Byte> ImageIdBytes = new ArrayList<>();
                                int k = j+i+4;
                                while(bytes[k]!='"')
                                {
                                    ImageIdBytes.add(bytes[k]);
                                    k++;
                                }
                                //Записываем найденный ID в строку
                                int ImageIdLength =ImageIdBytes.toArray().length;
                                byte[] ImageIdArrayBytes = new byte[ImageIdLength];
                                k=0;
                                for (byte elem:ImageIdBytes)
                                {
                                    ImageIdArrayBytes[k] = elem;
                                    k++;
                                }
                                //Записываю ImageIdNow - строка
                                String ImageIdNow = new String(ImageIdArrayBytes,"UTF-8");
                                //Сравниваю id
                                if (ImageIdNow.equals(id))
                                {
                                    //Записываю байты результат

                                    int start = 0;
                                    k = i+j+4+ImageIdLength+1;
                                    while(bytes[k]!='<')
                                    {
                                        if (bytes[k]=='>')
                                        {
                                            start =1;
                                        }
                                        else if (start==1)
                                        {
                                            ImageBytes.add(bytes[k]);
                                        }
                                        k++;
                                    }
                                    //возвожу фллаг выхода из главного цикла
                                    flagAllBreak = 1;
                                    //выхожу из этого цикла
                                }
                                else {
                                    //Если не совпало,выхожу из этого цикла
                                    //Break из вечного цикла, если Id неравны
                                }
                                break;
                            }
                            j++;
                        }
                    }
                }
                else if(flagAllBreak == 1)
                {
                    break;
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int arrayLenght = ImageBytes.toArray().length;
        /*----------------------------------------------------------------------------------------------*/
        byte[] resultBytes = new byte[arrayLenght];
        int i = 0;
        for (byte elem:ImageBytes)
        {
            resultBytes[i] = elem;
            i++;
        }
        res = resultBytes;
        //res = new String(resultBytes,"UTF-8");
        return res;
    }

}
