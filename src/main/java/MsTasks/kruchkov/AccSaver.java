package MsTasks.kruchkov;

import java.util.ArrayList;
import java.util.HashMap;

public final class AccSaver  {
    private String name;
    private ArrayList<CurrCount> accCurrCountS;
    private String saveName;

    public AccSaver(String name, ArrayList<CurrCount> accCurrCountS, String saveName)    {
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
    public ArrayList<CurrCount> getAccCurrCount()   {
        return this.accCurrCountS;
    }

    public void setAccCurrCount(ArrayList<CurrCount> accCurrCount)   {
        this.accCurrCountS = accCurrCount;
    }

    public String getSaveName() {
        return this.saveName;
    }
    public void  setSaveName(String saveName) {
        this.saveName=saveName;
    }
}
