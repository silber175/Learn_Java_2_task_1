package MsTasks.kruchkov;

import org.junit.Test;

import java.util.ArrayList;

public class Tests {
@Test
// Account creating with wrong nam
public void AccCreate() {

    Account vAccount = new Account("Иванов Иван Иванович");
    if (!vAccount.getName().equals("Иванов Иван Иванович")) {
        throw new RuntimeException("Error test : Account creating with wrong name");
    }
}
// Account on creating don't check for empty or null
    public void AccEmptyCreate()    {
    try {
        Account vAccount = new Account("");
    } catch (IllegalArgumentException err) {return;}
    throw new RuntimeException("Error test : Account on creating don't check for empty");
    }
    public void AccNullCreate()    {
        try {
        Account vAccount = new Account(null);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Account on creating don't check for null");
    }
// Не записывается валюта  либо количество
    public void CurrencyCreate()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        CurrCount vCurrCountUsd = new CurrCount("USD",2000);
        CurrCount vCurrCountEur = new CurrCount("EUR",1000);

        vAccount.changeBal(vCurrCountUsd);
        vAccount.changeBal(vCurrCountEur);
        ArrayList<CurrCount> vCurrCount = vAccount.getAccCurrCount();
        boolean vFoundUsd = false;
        for(int ii=0;ii< vCurrCount.size();ii++){
            if (vCurrCount.get(ii).getCurrency().equals("USD") &&
                    vCurrCount.get(ii).getCurCount() == 2000)   {
                vFoundUsd = true;
            }
            if(!vFoundUsd )    {
                throw new RuntimeException("Error test : Не записывается валюта 1 либо количество");

            }
        }
        boolean vFoundEur = false;
        for(int ii=0;ii< vCurrCount.size();ii++){
            if (vCurrCount.get(ii).getCurrency().equals("EUR") &&
                    vCurrCount.get(ii).getCurCount() == 1000)   {
                vFoundUsd = true;
            }
            if(!vFoundUsd )    {
                throw new RuntimeException("Error test : Не записывается валюта 2 либо количество");

            }
        }
    }
    // валюта не проверяется по списку допустимых значений
    public void CurrШтМфдшвCreate() {
        Account vAccount = new Account("Иванов Петр Иванович");

        CurrCount vCurrCountEur = new CurrCount("XXX",1000);

        try {
            vAccount.changeBal(vCurrCountEur);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : валюта не проверяется по списку допустимых значений");
    }
    // Количество валюты не проверяется на отрицательное значение
    public void CurrNegotCreate() {
        Account vAccount = new Account("Иванов Петр Иванович");

        CurrCount vCurrCountEur = new CurrCount("EUR",-1000);

        try {
        vAccount.changeBal(vCurrCountEur);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Количество валюты не проверяется на отрицательное значение");
    }
    // Проверка метод undo при попытке откатить без измененицй выдает ошибку
    public void undoNoChange() {
        Account vAccount = new Account("Иванов Петр Иванович");
        try {
            vAccount.undo();
        }
            catch (IllegalArgumentException err) {return;}
            throw new RuntimeException("Error test : метод undo при попытке откатить без измененицй не выдает ошибку");

    }
    // Проверка работы undo
    public void undoNWorkFrame()    {
        Account vAccount = new Account("Кузнецов Николай Петрович");

        CurrCount vCurrCountRur = new CurrCount("RUR",100);
        vAccount.changeBal(vCurrCountRur);
         vAccount.setName("Василий Иванов");

        vCurrCountRur = new CurrCount("RUR",300);
        vAccount.changeBal(vCurrCountRur);
        vAccount.undo();
        ArrayList<CurrCount> vCurrCount = vAccount.getAccCurrCount();

        boolean vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("RUR") )    {
                vFoundUsd = true;

                if (vCurrCount.get(ii).getCurCount() != 100) {
                    throw new RuntimeException("Error test : метод undo неверно откатывает 1 раз колчество валюты");
                }
            }
        }
            if (!vFoundUsd) {
                throw new RuntimeException("Error test : метод undo при откате колчества валюты , валюта удаляет из объекта");

            }


        vAccount.undo();
        if (!vAccount.getName().equals("Кузнецов Николай Петрович"))   {
            throw new RuntimeException("Error test : метод undo  неверно откатывает наименование");

        }
        vAccount.undo();
        vCurrCount = vAccount.getAccCurrCount();
        vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("RUR") )    {
                vFoundUsd = true;


            }
        }
            if (vFoundUsd) {
                throw new RuntimeException("Error test : метод undo при откате  валюты , валюта не пропадает из объекта");

            }

    }
    // Проверка метода сохранения
    public void savePointCheck()  {
        ArrayList<AccSaver> accSavers = new ArrayList<AccSaver>();
        Account vAccount = new Account("Сидоров И.К");
        CurrCount vCurrCountUsd = new CurrCount("USD",2000);
        CurrCount vCurrCountEur = new CurrCount("EUR",1000);

        vAccount.changeBal(vCurrCountUsd);
        vAccount.changeBal(vCurrCountEur);



        // CurrCount vCurrCountXXX = new CurrCount("XXX",1800);




        CurrCount vCurrCountEur2 = new CurrCount("EUR",1500);
        vAccount.changeBal(vCurrCountEur2);
        String aSaveName ="sp1";



        for (int ii = 0; ii < accSavers.size(); ii++) {
            if (accSavers.get(ii).getSaveName().equals(aSaveName)) {
                throw new IllegalArgumentException("С  именем "+aSaveName+" savepoint уже существует ");
            }
        }
        AccSaver vAccSaver =  save(vAccount, aSaveName);
        accSavers.add(vAccSaver);

        vAccount.setName("Иван Васильевич");



        vCurrCountEur2 = new CurrCount("EUR",3000);
        vAccount.changeBal(vCurrCountEur2);

        vCurrCountUsd = new CurrCount("USD",6000);


        vAccount.changeBal(vCurrCountUsd);
        aSaveName = "sp2";
        for (int ii = 0; ii < accSavers.size(); ii++) {
            if (accSavers.get(ii).getSaveName().equals(aSaveName)) {
                throw new IllegalArgumentException("С  именем "+aSaveName+" savepoint уже существует ");
            }
        }
        vAccSaver =  save(vAccount, aSaveName);
        accSavers.add(vAccSaver);
        vAccount.setName("Pol Robson");



        restore(vAccount,"sp1", accSavers);
        if (!vAccount.getName().equals("Сидоров И.К") )   {
            throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
        }
        ArrayList<CurrCount> vCurrCount = vAccount.getAccCurrCount();
        boolean vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("USD") )    {
                vFoundUsd = true;

                if (vCurrCount.get(ii).getCurCount() != 2000) {
                    throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
                }
            }
        }
        if (!vFoundUsd) {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }

        vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("EUR") )    {
                vFoundUsd = true;

                if (vCurrCount.get(ii).getCurCount() != 1500) {
                    throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
                }
            }
        }
        if (!vFoundUsd) {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }


        restore(vAccount,"sp2", accSavers);


        if (!vAccount.getName().equals("Иван Васильевич") )   {
            throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
        }
        vCurrCount = vAccount.getAccCurrCount();
         vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("USD") )    {
                vFoundUsd = true;

                if (vCurrCount.get(ii).getCurCount() != 6000) {
                    throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
                }
            }
        }
        if (!vFoundUsd) {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }

        vFoundUsd = false;
        for(int ii=0;ii<vCurrCount.size();ii++)  {
            if (vCurrCount.get(ii).getCurrency().equals("EUR") )    {
                vFoundUsd = true;

                if (vCurrCount.get(ii).getCurCount() != 3000) {
                    throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
                }
            }
        }
        if (!vFoundUsd) {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }



    }
    public static AccSaver save(AccSavable pAcnt, String aSaveName)  {
        return pAcnt.save(aSaveName);
    }
    public  static void restore(AccSavable pAcnt, String aSaveName, ArrayList<AccSaver> accSavers) {
        pAcnt.restore(aSaveName, accSavers);
    }
    // Проверка метода undo при добавлении поля вид счета в класс Account
    // При наличии поля private String acntType  тесты проходят



}
