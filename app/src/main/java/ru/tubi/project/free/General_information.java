package ru.tubi.project.free;

public class General_information {

    //сделать обновление master из версии разработчика

    /*
    БД
    сверить БД, таблицы, столбцы все в наличии

    ANDROID STUDIO
    main_activity убрать слово-"тест"
    в build.gradle app поменять versionCode и при необходимости versionName
    сделать synk now
    сменить URL в (Config) для запроса в рабочем PHP
    установить проверку номера телефона
    сделать релиз
    залить релиз в Google play

    PHP
    в PHP сменить имя БД на h102582557_tubi
    на сервере в api_tubi удалить все файлы php, а папки с фото не трогать(image, preview_image)
    залить обновление в api_tubi

    //создать новые ветки с будущим номером релиза

     */

                        //---------------
    /*
    Один раз в день после 3 часов ночи при посещении приложения срабатывает скрипт
    обновления состояния заказов t_order 'prder_active' и (отмечает все невыполненные заказы '0')
    удаляет все заказы которые не были отправлены в обработку
    Путь к скрипту начинается в connect.php

            Так же в это же время приложение тоже обнуляет order_id


     */
}
