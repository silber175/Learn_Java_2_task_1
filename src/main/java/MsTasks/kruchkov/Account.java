package MsTasks.kruchkov;

import com.sun.nio.sctp.IllegalReceiveException;

import java.util.ArrayList;
import java.util.HashMap;


public class Account implements AccSavable{
    private String name;
    private ArrayList<CurrCount> accCurrCount;
   // private ArrayList<AccSaver> accSavers;
    private AccChngLog vAccChngLog;
    private AccChngLog privMeth;

    private String acntType;     // Для дальнейшего развития счета добавлен тип  
    
   
    
    
    public Account(String vName) {
        if (vName == null)
            throw new IllegalArgumentException("Имя не может быть null ");
        if (vName.length() == 0)
            throw new IllegalArgumentException("Имя не может быть пустым ");
        this.name = vName;
        this.accCurrCount = new ArrayList<CurrCount>();
     //   this.accSavers = new ArrayList<AccSaver>();
        privMeth =null;
        vAccChngLog = new AccChngLog(this.accCurrCount, this.name, privMeth);

    }

    public String getName() {
        return this.name;

    }

    public void setName(String vName) {

        this.name = vName;
        privMeth =vAccChngLog;
        vAccChngLog = new AccChngLog(null, this.name, privMeth);
    }

    private void undoName(String vName) {

        this.name = vName;

    }

    public ArrayList<CurrCount> getAccCurrCount() {
        return this.accCurrCount;
    }
/*
    public ArrayList<AccSaver> getAccSavers() {
        return this.accSavers;
    }

 */

    public void changeBal(CurrCount vCurrCount) {


        boolean vFound = false;
        for (int ii = 0; ii < this.accCurrCount.size(); ii++) {
            if (vCurrCount.getCurrency().equals(this.accCurrCount.get(ii).getCurrency())) {
                vFound = true;
              this.accCurrCount.get(ii).setCurCount(vCurrCount.getCurCount());

                break;
            }
        }
        if (!vFound) {


            this.accCurrCount.add(vCurrCount);

        }
        privMeth =vAccChngLog;
        vAccChngLog = new AccChngLog(this.accCurrCount, this.name, privMeth);

    }

    // Метод для тестирования результатов изменений валюты
    public String printAccCurrCount() {
        String sRes = " ";
        for (int ii = 0; ii < this.accCurrCount.size(); ii++) {
            if (ii > 0) {
                sRes = sRes + " ";
            }
            sRes = sRes + "Валюта " + this.accCurrCount.get(ii).getCurrency() +
                    " количество " + this.accCurrCount.get(ii).getCurCount() + '\n';
        }
        return sRes;
    }

    public AccSaver save(String aSaveName)

    {

        if (aSaveName == null | aSaveName.length() == 0) {
            throw new IllegalArgumentException("Имя savepoint не может быть пустым ");
        }


        ArrayList<CurrCount> vAccCurrCount = new ArrayList<CurrCount>();
        for(int ii=0;ii<this.accCurrCount.size();ii++){

            vAccCurrCount.add(  new CurrCount( this.accCurrCount.get(ii).getCurrency(),
                    this.accCurrCount.get(ii).getCurCount() ));


        }
        AccSaver acSv=new AccSaver(this.name, vAccCurrCount, aSaveName);

      //  this.accSavers.add(acSv);

        return acSv;
    }

    // Откат на сохраненное состояние с именем aSaveName ля тестирования
    public void restore(String aSaveName, ArrayList<AccSaver> accSavers) {
        //   ArrayList<AccSaver> acSvArr = this.accSavers;
        String name=null;
        ArrayList<CurrCount> vAccCurrCount = new ArrayList<CurrCount>();
        boolean vFound = false;
        for (int ii = 0; ii < accSavers.size(); ii++) {
            if (accSavers.get(ii).getSaveName().equals( aSaveName)) {
               name = accSavers.get(ii).getName();
               vAccCurrCount=accSavers.get(ii).getAccCurrCount();
                for(int jj=0;jj<vAccCurrCount.size();jj++){
          //          System.out.println("Debug1:: "+vAccCurrCount[jj].getCurCount());
                }
               this.name = name;
               this.accCurrCount = vAccCurrCount;
         //       System.out.println("Debug2:: Name "+this.accSavers.get(ii).getSaveName()+" Object "+
          //              this.accSavers.get(ii).getName()+" "+name);
                vFound = true;
                break;
            }
        }
        if (!vFound)    {
            throw new IllegalArgumentException("С  именем "+aSaveName+" savepoint не существует ");
        }

    }
    private class AccChngLog implements  Undoable   {
        public AccChngLog prevMeth;
        private ArrayList<CurrCount> vAccCurrCount;
        private String  name;
        public AccChngLog(ArrayList<CurrCount> vAccCurrCount, String  name, AccChngLog prevMeth)    {
            if (vAccCurrCount == null)   {
                this.vAccCurrCount = null;
            }
            else {
                ArrayList<CurrCount> nAccCurrCount = new ArrayList<CurrCount>();
                for (int ii = 0; ii < vAccCurrCount.size(); ii++) {
                    nAccCurrCount.add(new CurrCount(vAccCurrCount.get(ii).getCurrency(),
                            vAccCurrCount.get(ii).getCurCount()));
                    //   System.out.println("Debug4:: "+vAccCurrCount.get(ii).getCurrency()+"  "+
                    //           vAccCurrCount.get(ii).getCurCount());

                }
                this.vAccCurrCount = nAccCurrCount;
            }
            if (name==null) {
                this.name = null;
            }
            else {
                this.name = name;
            }
            this.prevMeth=prevMeth;
        }
        public ArrayList<CurrCount> getVAccCurrCount()   {
            return this.vAccCurrCount;
        }
        public String  getName()    {
            return this.name;
        }
        public void nextMeth(Account vAcnt)  {
            if (this.prevMeth == null)   {
                throw new IllegalReceiveException ("Попытка отменить несуществующие именения");
            }
            AccChngLog currMeth = vAccChngLog;
            vAccChngLog=vAccChngLog.prevMeth;

            // ткатываем валюту или количество
            if (currMeth.vAccCurrCount==null)   {
                vAcnt.accCurrCount = vAccChngLog.getVAccCurrCount();
            }

            // подлежит откату наименование
            if (currMeth.name == null) {
                vAcnt.undoName(vAccChngLog.getName());
            }
            // Здесь будет undo для типа счета

        }
    }
    private  void undoS(Undoable vObj, Account vAcnt)    {
        vObj.nextMeth(vAcnt);
    }
    public void undo()  {

   //     vAccChngLog.nextMeth(this);
        undoS(vAccChngLog, this);
    }




}

