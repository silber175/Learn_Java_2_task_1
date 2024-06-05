package MsTasks.kruchkov;

import java.util.ArrayList;

public interface AccSavable {
    // Интерфейс для сохранения
         //       AccSaver setAcnt(Object acnt);
  //      AccSaver aSave(Object acnt, String saveName) throws CloneNotSupportedException;
//        AccSaver setsaveName(String saveName);

    AccSaver save(String aSaveName);
    void restore(String aSaveName, ArrayList<AccSaver> accSavers) ;


}
