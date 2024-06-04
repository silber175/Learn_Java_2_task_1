package MsTasks.kruchkov;

public interface AccSavable {
    // Интерфейс для сохранения
         //       AccSaver setAcnt(Object acnt);
  //      AccSaver aSave(Object acnt, String saveName) throws CloneNotSupportedException;
//        AccSaver setsaveName(String saveName);

    void save(String aSaveName);
    void restore(String aSaveName);


}
