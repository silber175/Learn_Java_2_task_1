package MsTasks.kruchkov;

import java.util.ArrayList;

public class KruchkovTask1 {
    public static void main(String[] args)
            throws CloneNotSupportedException
    {
        ArrayList<AccSaver> accSavers = new ArrayList<AccSaver>();

        System.out.println("Task  call example");
        // Simple test
        Account vAccount = new Account("Сидоров И.К");
        CurrCount vCurrCountUsd = new CurrCount("USD",2000);
        CurrCount vCurrCountEur = new CurrCount("EUR",1000);

        vAccount.changeBal(vCurrCountUsd);
        vAccount.changeBal(vCurrCountEur);

        String vRes = vAccount.printAccCurrCount() ;
        System.out.println(vRes);
        System.out.println(vAccount.getName());

        // CurrCount vCurrCountXXX = new CurrCount("XXX",1800);
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("Before save point 1 "+vRes);
        System.out.println(vAccount.getName());
        CurrCount vCurrCountEur2 = new CurrCount("EUR",1500);
        AccSaver vAccSaver = save(vAccount, "sp1");
        accSavers.add(vAccSaver);
        System.out.println("Call save point 1");

        vAccount.changeBal(vCurrCountEur2);
        vAccount.setName("Иван Васильевич");

        vRes = vAccount.printAccCurrCount() ;
        System.out.println("After change"+vRes);
        System.out.println(vAccount.getName());

        vCurrCountEur2 = new CurrCount("EUR",3000);
        vAccount.changeBal(vCurrCountEur2);
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("Before save point 2"+vRes);
        System.out.println(vAccount.getName());
        vAccSaver = save(vAccount, "sp2");
        accSavers.add(vAccSaver);
        System.out.println("Call save point 2");


        vAccount.undo();
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("1 undo "+vRes);
        System.out.println(vAccount.getName());

        vAccount.undo();
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("2 undo "+vRes);
        System.out.println(vAccount.getName());

        vAccount.setName("Pol Robson");

        System.out.println("Before restore save points"+vRes);
        System.out.println(vAccount.getName());

        restore(vAccount,"sp2", accSavers);
        System.out.println("Call restore save point 2");
        System.out.println("After restore save point 2"+vRes);
        System.out.println(vAccount.getName());

        restore(vAccount,"sp1", accSavers);
        System.out.println("Call restore save point 1");

        System.out.println("After restore save point 1"+vRes);
        System.out.println(vAccount.getName());
    }
    public static AccSaver save(AccSavable pAcnt, String aSaveName)  {
        return pAcnt.save(aSaveName);
    }
    public  static void restore(AccSavable pAcnt, String aSaveName, ArrayList<AccSaver> accSavers) {
        pAcnt.restore(aSaveName, accSavers);
    }
};

