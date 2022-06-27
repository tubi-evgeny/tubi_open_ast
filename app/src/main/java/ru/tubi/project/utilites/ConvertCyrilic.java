package ru.tubi.project.utilites;

public class ConvertCyrilic {

    public String ConvertCyrilic(String message) {
        //char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','ѕ','и','ј','к','л','љ','м','н','њ','о','п','р','с','т', 'ќ','у', 'ф','х','ц','ч','џ','ш', 'А','Б','В','Г','Д','Ѓ','Е', 'Ж','З','Ѕ','И','Ј','К','Л','Љ','М','Н','Њ','О','П','Р','С','Т', 'Ќ', 'У','Ф', 'Х','Ц','Ч','Џ','Ш','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','/','-'};
        //String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","y","i","j","k","l","q","m","n","w","o","p","r","s","t","'","u","f","h", "c",";", "x","{","A","B","V","G","D","}","E","Zh","Z","Y","I","J","K","L","Q","M","N","W","O","P","R","S","T","KJ","U","F","H", "C",":", "X","{", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","/","-"};
        char[] abcCyr = {' ','А','Б','В','Г','Д','Е','Ё', 'Ж', 'З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ч', 'Ц','Ш', 'Щ', 'Э','Ю', 'Я', 'Ы','Ъ', 'Ь', 'а','б','в','г','д','е','ё', 'ж', 'з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ч', 'ц','ш',  'щ',  'э', 'ю','я', 'ы','ъ','ь','-'};
        String[] abcLat = {" ","A","B","V","G","D","E","JO","ZH","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","H","CH","C","SH","CSH","E","JU","JA","Y","", "", "a","b","v","g","d","e","jo","zh","z","i","j","k","l","m","n","o","p","r","s","t","u","f","h","ch","c","sh","csh","e","ju","ja","y", "", "","_"};

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }
}
