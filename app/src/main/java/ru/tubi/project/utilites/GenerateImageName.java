package ru.tubi.project.utilites;

public class GenerateImageName {

    private String category;
    private String brand;
    private String characteristic;

    public String generateImageName(String category,
                                    String productName,String characteristic,String brand){
        //удалить из строки все символы кроме букв и цифр
        String tempCategory = deleteBadSimbolFromString(category);
        String tempProductName = deleteBadSimbolFromString(productName);
        String tempBrand = deleteBadSimbolFromString(brand);
        String tempCharacteristic = deleteBadSimbolFromString(characteristic);

        String imageName=tempCategory + tempProductName + tempCharacteristic
                            + tempBrand + System.currentTimeMillis();
        //String imageName=tempCategory + tempBrand+tempCharacteristic+ System.currentTimeMillis() + ".jpg";

        return imageName;
    }

    //удалить из строки все символы кроме букв и цифр
    private String deleteBadSimbolFromString(String str) {

        String[]string = str.split("[' '|\\-]+");//"[-.]+"
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < string.length; i++){
            //делаем все буквы строчными
            String st = string[i].toLowerCase();
            //удалить из слов все символы кроме букв и цифр в массив
            String strClear = st.replaceAll("[^\\p{L}\\p{N}]+", "");
            //заменить всю кирилицу на латиницу

            ConvertCyrilic convertCyrilic = new ConvertCyrilic();
            String strLatin = convertCyrilic.ConvertCyrilic(strClear);
            // String strLatin = convertCyrilic(strClear);
            //собрать строку с подчеркиванием "_"
            builder.append(strLatin+"_");

            //удалить из строки все символы кроме букв и цифр
            // builder.append(st.replaceAll("[^\\p{L}\\p{N}]+", "")+"_");

        }

        String myStrLatin = builder.toString();
        return myStrLatin;
    }
}
