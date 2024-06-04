package MsTasks.kruchkov;

public class CurrCount {
    private String currency;
    private int curCount;

    public CurrCount(String currency, int curCount) {
        String[] validCurrArr = {"USD", "EUR", "GBP","JPY", "RUR"};
        boolean vFound=false;
        for (int ii=0;ii < validCurrArr.length;ii++) {
            if (currency == validCurrArr[ii]) {
                vFound = true;
                break;
            }
        }
        if (curCount < 0) throw new IllegalArgumentException("Количество валюты не может быть отрицательным");
        if( vFound  ) {

            this.currency = currency;
        this.curCount = curCount;
        }
        else    {
            throw new IllegalArgumentException( "Ошибка : Недопустиый код валюты " + currency);

        }
    }
    public String getCurrency() {
            return this.currency;
    }
    public void setCurrency( String  currency) {
         this.currency = currency;
    }
    public int getCurCount()    {
            return this.curCount;
    }
    public void setCurCount(int curCount)   {
            this.curCount = curCount;

   }
}
