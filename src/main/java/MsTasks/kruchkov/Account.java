package MsTasks.kruchkov;

import com.sun.nio.sctp.IllegalReceiveException;


public class Account implements AccSavable{
    private String name;
    private CurrCount[] accCurrCount;
    private AccSaver[] accSavers;
    private AccChngLog vAccChngLog;
    private AccChngLog privMeth;

    //   private String acntVid;

    private final class AccSaver  {
        private String name;
        private CurrCount[] accCurrCountS;
        private String saveName;

       public AccSaver(String name,  CurrCount[] accCurrCountS, String saveName)    {
            this.name = name;
            this.accCurrCountS = accCurrCountS;
            this.saveName = saveName;;
        }

        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name=name;
        }
        public CurrCount[] getAccCurrCount()   {
            return this.accCurrCountS;
        }

        public void setAccCurrCount(CurrCount[] accCurrCount)   {
            this.accCurrCountS = accCurrCount;
        }

        public String getSaveName() {
            return this.saveName;
        }
        public void  setSaveName(String saveName) {
            this.saveName=saveName;
        }
    }

    public Account(String vName) {
        if (vName == null)
            throw new IllegalArgumentException("Имя не может быть null ");
        if (vName.length() == 0)
            throw new IllegalArgumentException("Имя не может быть пустым ");
        this.name = vName;
        this.accCurrCount = new CurrCount[0];
        this.accSavers = new AccSaver[0];
        privMeth =null;
        vAccChngLog = new AccChngLog(this.accCurrCount, this.name, privMeth);

    }

    public String getName() {
        return this.name;

    }

    public void setName(String vName) {

        this.name = vName;
        privMeth =vAccChngLog;
        vAccChngLog = new AccChngLog(this.accCurrCount, this.name, privMeth);
    }

    private void undoName(String vName) {

        this.name = vName;

    }

    public CurrCount[] getAccCurrCount() {
        return this.accCurrCount;
    }

    public AccSaver[] getAccSavers() {
        return this.accSavers;
    }

    public void changeBal(CurrCount vCurrCount) {


        boolean vFound = false;
        for (int ii = 0; ii < this.accCurrCount.length; ii++) {
            if (vCurrCount.getCurrency().equals(this.accCurrCount[ii].getCurrency())) {
                vFound = true;
                this.accCurrCount[ii].setCurCount(vCurrCount.getCurCount());
                break;
            }
        }
        if (!vFound) {

            CurrCount[] nAcCurrCount = new CurrCount[this.accCurrCount.length + 1];
            for (int ii = 0; ii < this.accCurrCount.length; ii++) {
                nAcCurrCount[ii] = this.accCurrCount[ii];
            }

            nAcCurrCount[this.accCurrCount.length] = vCurrCount;
            this.accCurrCount = nAcCurrCount;

        }
        privMeth =vAccChngLog;
        vAccChngLog = new AccChngLog(this.accCurrCount, this.name, privMeth);

    }

    // Метод для тестирования результатов изменений валюты
    public String printAccCurrCount() {
        String sRes = " ";
        for (int ii = 0; ii < this.accCurrCount.length; ii++) {
            if (ii > 0) {
                sRes = sRes + " ";
            }
            sRes = sRes + "Валюта " + this.accCurrCount[ii].getCurrency() +
                    " количество " + this.accCurrCount[ii].getCurCount() + '\n';
        }
        return sRes;
    }

    public void save(String aSaveName)

    {

        if (aSaveName == null | aSaveName.length() == 0) {
            throw new IllegalArgumentException("Имя savepoint не может быть пустым ");
        }

        for (int ii = 0; ii < this.accSavers.length; ii++) {
            if (this.accSavers[ii].getSaveName().equals(aSaveName)) {
                throw new IllegalArgumentException("С  именем "+aSaveName+" savepoint уже существует ");
            }
        }


        CurrCount[] vAccCurrCount = new CurrCount[this.accCurrCount.length];
        for(int ii=0;ii<this.accCurrCount.length;ii++){

            vAccCurrCount[ii]= new CurrCount( this.accCurrCount[ii].getCurrency(),
                    this.accCurrCount[ii].getCurCount() );


        }
        AccSaver acSv=new AccSaver(this.name, vAccCurrCount, aSaveName);
        AccSaver[] acSvArr = new AccSaver[this.accSavers.length + 1];
        for (int ii = 0; ii < this.accSavers.length; ii++) {
            acSvArr[ii] = this.accSavers[ii];
        }
        acSvArr[this.accSavers.length] = acSv;
        this.accSavers = acSvArr;

        // debug temp
        /*              for (int ii = 0; ii < this.accSavers.length; ii++) {
           System.out.println("Debug:: Name "+this.accSavers[ii].getSaveName()+" Object "+
                   this.accSavers[ii].getName());


        }

         */
    }

    // Откат на сохраненное состояние с именем aSaveName ля тестирования
    public void restore(String aSaveName) {
        //   AccSaver[] acSvArr = this.accSavers;
        String name=null;
        CurrCount[] vAccCurrCount = new CurrCount[0];
        boolean vFound = false;
        for (int ii = 0; ii < this.accSavers.length; ii++) {
            if (this.accSavers[ii].getSaveName().equals( aSaveName)) {
               name = this.accSavers[ii].getName();
               vAccCurrCount=this.accSavers[ii].getAccCurrCount();
                for(int jj=0;jj<vAccCurrCount.length;jj++){
          //          System.out.println("Debug1:: "+vAccCurrCount[jj].getCurCount());
                }
               this.name = name;
               this.accCurrCount = vAccCurrCount;
         //       System.out.println("Debug2:: Name "+this.accSavers[ii].getSaveName()+" Object "+
          //              this.accSavers[ii].getName()+" "+name);
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
        private CurrCount[] vAccCurrCount;
        private String  name;
        public AccChngLog(CurrCount[] vAccCurrCount, String  name, AccChngLog prevMeth)    {
            CurrCount[] nAccCurrCount = new CurrCount[vAccCurrCount.length];
            for (int ii=0;ii<vAccCurrCount.length;ii++ )  {
                nAccCurrCount[ii] = new CurrCount( vAccCurrCount[ii].getCurrency(),
                        vAccCurrCount[ii].getCurCount() );
             //   System.out.println("Debug4:: "+vAccCurrCount[ii].getCurrency()+"  "+
             //           vAccCurrCount[ii].getCurCount());

            }
            this.vAccCurrCount = nAccCurrCount;

            this.name = name;
            this.prevMeth=prevMeth;
        }
        public CurrCount[] getVAccCurrCount()   {
            return this.vAccCurrCount;
        }
        public String  getName()    {
            return this.name;
        }
        public void nextMeth(Account vAcnt)  {
            if (this.prevMeth == null)   {
                throw new IllegalReceiveException ("Попытка отменить несуществующие именения");
            }
            vAccChngLog=vAccChngLog.prevMeth;

            vAcnt.accCurrCount = vAccChngLog.getVAccCurrCount();
            vAcnt.undoName(vAccChngLog.getName());
            // Здесь будет undo для вида счета
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

