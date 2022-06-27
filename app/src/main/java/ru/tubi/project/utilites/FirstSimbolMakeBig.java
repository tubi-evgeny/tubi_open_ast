package ru.tubi.project.utilites;


//user_name = new FirstSimbolMakeBig().firstSimbolMakeBig(user_name);
public class FirstSimbolMakeBig {
    private String string;

    public String firstSimbolMakeBig(String string){

        if(string == null || string.isEmpty()) return string;
        return string.substring(0,1).toUpperCase()+ string.substring(1);
    }

}
