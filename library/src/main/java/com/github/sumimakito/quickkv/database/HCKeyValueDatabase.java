/**
 * QucikKV
 * Copyright 2014-2016 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 */

package com.github.sumimakito.quickkv.database;

import com.github.sumimakito.quickkv.QuickKV;
import com.github.sumimakito.quickkv.util.DataProcessor;
import com.github.sumimakito.quickkv.util.QKVLogger;

import java.io.*;
import java.util.*;

public class HCKeyValueDatabase extends QKVDB implements QKVDBImpl {

    //Header of HC-KVDB File
    public static final String[] HEADER = {"QuickKV/HCKeyValueDatabase", "+Reserved", "+Reserved", "+Reserved"};
    public static final int HEADER_HEIGHT = HEADER.length;

    private final File fOriginalDB;
    private File fDatabase;
    private Seeker seeker;
    private ArrayList<Task> tasks = new ArrayList<Task>();

    public HCKeyValueDatabase(QuickKV quickKV, String pDBFileName) {
        super(quickKV, null);
        fDatabase = new File(quickKV.getStorageManager().getWorkspace(), pDBFileName);
        fOriginalDB = fDatabase;
        if (!fOriginalDB.exists()) {
            try {
                initializeDB();
            } catch (Exception e) {
                QKVLogger.ex(e);
            }
        }
        seeker = new Seeker(fOriginalDB);
    }

    private void initializeDB() throws Exception {
        FileOutputStream fos = new FileOutputStream(fOriginalDB.getAbsolutePath());
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fos));
        writeHeader(w);
        w.close();
    }

    @Override
    public <K, V> boolean put(K k, V v) {
        try {
            int pos = seeker.searchForKey(k);
            if (pos == -1) {
                addTask(new Task(Task.Type.New, -1, k, v));
                //newKVsMap.put(DataProcessor.Persistable.addPrefix(k), DataProcessor.Persistable.addPrefix(v));
            } else {
                addTask(new Task(Task.Type.Replace, pos, k, v));
                //rplcMap.put(pos + 1, DataProcessor.Persistable.addPrefix(v));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <K, V> boolean putBlindly(K k, V v) {
        try {
            addTask(new Task(Task.Type.New, -1, k, v));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <K, V> boolean put(HashMap<K, V> map) {
        try {
            List<Object> keys = new ArrayList<Object>();
            if (map.size() > 0) {
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    keys.add(entry.getKey());
                }
            }
            Object[] keyObjs = new Object[keys.size()];
            int i = 0;
            for (Object o : keys) {
                keyObjs[i++] = o;
            }
            Map<Object, Integer> rMap = seeker.searchForKeys(keyObjs);
            for (Object o : keys) {
                if (rMap.containsKey(o)) {
                    int pos = rMap.get(o);
                    addTask(new Task(Task.Type.Replace, pos, o, map.get(o)));
                    //rplcMap.put(pos, DataProcessor.Persistable.addPrefix(map.get(o)));
                } else {
                    addTask(new Task(Task.Type.New, -1, o, map.get(o)));
                    //newKVsMap.put(DataProcessor.Persistable.addPrefix(o), DataProcessor.Persistable.addPrefix(map.get(o)));
                }
            }

            return true;
        } catch (Exception e) {
            QKVLogger.ex(e);
            return false;
        }
    }

    @Override
    public <K> Object get(K k) {
        try {
            for (Task t : tasks) {
                if (t.getObj1().equals(k)) {
                    switch (t.getType()) {
                        case New:
                            return t.getObj2();
                        case Remove:
                            return null;
                        case Replace:
                            return t.getObj2();
                    }
                }
            }
            Object result = seeker.getValueByKey(k);
            return result;
        } catch (Exception e) {
            QKVLogger.ex(e);
            return null;
        }
    }

    @Override
    public <K> boolean containsKey(K k) {
        try {
            if (seeker.searchForKey(k) != -1) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            QKVLogger.ex(e);
            return false;
        }
    }

    @Deprecated
    @Override
    public <V> boolean containsValue(V v) {
        return false;
    }

    @Override
    public <K> boolean remove(K k) {
        try {
            int r = seeker.searchForKey(k);
            if (r != -1) {
                //rmList.add(r);
                //rmKeys.put(r, DataProcessor.Persistable.addPrefix(k));
                addTask(new Task(Task.Type.Remove, r, k, null));
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <K> boolean remove(K[] k) {
        try {
            Map<Object, Integer> rMap = seeker.searchForKeys(k);
            for (Object o : k) {
                if (rMap.containsKey(o)) {
                    //rmList.add(rMap.get(o));
                    //rmKeys.put(rMap.get(o), DataProcessor.Persistable.addPrefix(o));
                    addTask(new Task(Task.Type.Remove, rMap.get(o), o, null));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void clear() {

    }

    public boolean persist() {
        try {

            FileOutputStream fos = new FileOutputStream(fOriginalDB.getAbsolutePath() + ".buffer");
            int lPtr = 0;
            String line;
            BufferedReader r = new BufferedReader(new FileReader(fOriginalDB));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fos));
            //w.write(DataWrapper.HEADER);
            //seeker.skipHeader(r);


            ArrayList<Task> tNew = getTasksByType(Task.Type.New);
            /*
            ArrayList<Task> tRemove = getTasksByType(Task.Type.Remove);
            ArrayList<Task> tReplace = getTasksByType(Task.Type.Replace);
            */

            HashMap<Integer, Task> taskMap = new HashMap<Integer, Task>();

            int tempId = -1;
            for (Task t : tasks) {
                taskMap.put(t.getKp()!=-1?t.getKp():tempId--, t);
            }

            while ((line = r.readLine()) != null) {
                if (taskMap.containsKey(lPtr)) {
                    Task task = taskMap.get(lPtr);
                    switch (task.getType()) {
                        case Remove:
                            if (!tNew.isEmpty()) {
                                Task _task = tNew.get(0);
                                tNew.remove(0);
                                String key = DataProcessor.Persistable.addPrefix(_task.getObj1());
                                String val = DataProcessor.Persistable.addPrefix(_task.getObj2());
                                w.write(key);
                                w.newLine();
                                w.write(val);
                                w.newLine();
                            }
                            r.readLine(); //Skip
                            lPtr += 2;
                            break;
                        case Replace:
                            w.write(line);
                            w.newLine();
                            w.write(DataProcessor.Persistable.addPrefix(task.getObj2()));
                            w.newLine();
                            r.readLine(); //Skip
                            lPtr += 2;
                            break;
                    }
                } else {
                    w.write(line);
                    w.newLine();
                    lPtr++;
                }
            }
            r.close();

            for (Task t : tNew) {
                w.write(DataProcessor.Persistable.addPrefix(t.getObj1()));
                w.newLine();
                w.write(DataProcessor.Persistable.addPrefix(t.getObj2()));
                w.newLine();
            }
            w.close();

            File buffer = new File(fOriginalDB.getAbsolutePath() + ".buffer");
            buffer.renameTo(fOriginalDB);
            buffer.delete();

            tasks.clear();
            seeker.countLines();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addTask(Task task) {
        /*
        System.out.println("==>addTask():received");
        DataProcessor.printFieldsOfObject(task);

        System.out.println("==>addTask():before");
        for(Task _t:tasks){
            DataProcessor.printFieldsOfObject(_t);
        }
        */

        for (Task t : tasks) {
            if (t.getObj1().equals(task.getObj1())) {
                switch (task.getType()) {
                    //NEW task prop
                    case New:
                        switch (t.getType()) {
                            //OLD task prop
                            case Remove:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.Replace, t.getKp(), task.getObj1(), task.getObj2()));
                                return;
                            case New:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.New, -1, task.getObj1(), task.getObj2()));
                                return;
                            case Replace:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.Replace, t.getKp(), task.getObj1(), task.getObj2()));
                                return;
                            default:
                                return;
                        }
                    case Remove:
                        switch (t.getType()) {
                            //OLD task prop
                            case Remove:
                                System.out.println("OMG!! This is impossible!!");
                                return;
                            case New:
                                tasks.remove(t);
                                return;
                            case Replace:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.Remove, t.getKp(), task.getObj1(), null));
                                return;
                            default:
                                return;
                        }
                    case Replace:
                        switch (t.getType()) {
                            //OLD task prop
                            case Remove:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.Replace, t.getKp(), task.getObj1(), task.getObj2()));
                                return;
                            case New:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.New, -1, task.getObj1(), task.getObj2()));
                                return;
                            case Replace:
                                tasks.remove(t);
                                tasks.add(new Task(Task.Type.Replace, t.getKp(), task.getObj1(), task.getObj2()));
                                return;
                            default:
                                return;
                        }
                }
            }
        }
        tasks.add(task);

        /*
        System.out.println("==>addTask():after");
        for(Task _t:tasks){
            DataProcessor.printFieldsOfObject(_t);
        }*/
    }

    private int getTaskSize() {
        int c = 0;
        for (Task t : tasks) {
            switch (t.getType()) {
                case New:
                    c++;
                    break;
                case Remove:
                    c--;
                    break;
            }
        }
        return c;
    }

    private ArrayList<Task> getTasksByType(Task.Type type) {
        ArrayList<Task> result = new ArrayList<Task>();
        for (Task t : tasks) {
            if (type == t.getType()) {
                result.add(t);
            }
        }
        return result;
    }

    private void writeHeader(BufferedWriter w) throws IOException {
        for (String h : HEADER) {
            w.write(h);
            w.newLine();
        }
        w.newLine();
    }

    @Override
    public int size() {
        return seeker.getDataEntries() + getTaskSize();
    }

    private class Seeker {
        private File inFile;

        private int lineCount = 0;
        private int dataLines = 0;
        private int dataEntries = 0;

        public Seeker(File file) {
            try {
                inFile = file;
                countLines();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public int getDataEntries() {
            return dataEntries;
        }

        public void countLines() throws Exception {
            BufferedReader r = new BufferedReader(new FileReader(inFile));
            int lines = 0;
            while (r.readLine() != null) lines++;
            r.close();
            lineCount = lines;
            dataLines = lineCount - HEADER_HEIGHT - 1;
            if (dataLines % 2 != 0) {
                throw new Exception("Invalid data body!");
            } else {
                dataEntries = dataLines / 2;
            }
            //System.out.println(lines + " lines in total.");
        }

        public void skipHeader(BufferedReader r) throws IOException {
            for (int i = 0; i < HEADER_HEIGHT - 1; i++) r.readLine();
        }

        private int dataEntryOffsetToULine(int offset) {
            //3+2x...
            return (2*offset) - 1 + HEADER_HEIGHT;
        }

        private int searchForKey(Object object) throws IOException {
            String key = DataProcessor.Persistable.addPrefix(object);
            BufferedReader r = new BufferedReader(new FileReader(inFile));
            skipHeader(r);
            int offset;
            for (offset = 0; offset < dataEntries; offset++) {
                String k = r.readLine();
                //System.out.println("line -> "+k+" @offset:"+offset+" @UL:"+dataEntryOffsetToULine(offset));
                if (key.equals(k)) {
                    r.close();
                    //System.out.println("found record");
                    return dataEntryOffsetToULine(offset);
                }
                r.readLine();
            }
            r.close();
            return -1;
        }

        private Map<Object, Integer> searchForKeys(Object[] objects) throws Exception {
            Map<Object, Integer> result = new HashMap<Object, Integer>();
            String[] keys = new String[objects.length];
            for (int i = 0; i < objects.length; i++) {
                keys[i] = DataProcessor.Persistable.addPrefix(objects[i]);
            }

            BufferedReader r = new BufferedReader(new FileReader(inFile));
            skipHeader(r);
            int offset;
            for (offset = 0; offset < dataEntries; offset++) {
                String k = r.readLine();
                //System.out.println("line -> "+k+" @offset:"+offset+" @UL:"+dataEntryOffsetToULine(offset));
                for (String kt : keys) {
                    if (kt.equals(k)) {
                        //System.out.println("found record");
                        result.put(DataProcessor.Persistable.dePrefix(kt), dataEntryOffsetToULine(offset));
                    }
                }
                r.readLine();
            }
            r.close();
            return result;
        }

        public Object getValueByKey(Object k) throws Exception {
            String key = DataProcessor.Persistable.addPrefix(k);
            BufferedReader r = new BufferedReader(new FileReader(inFile));
            skipHeader(r);
            int offset;
            for (offset = 0; offset < dataEntries; offset++) {
                if (key.equals(r.readLine())) {
                    String result = r.readLine();
                    r.close();
                    return DataProcessor.Persistable.dePrefix(result);
                }
                r.readLine();
            }
            r.close();
            return null;
        }

        private List<Object> getKeys(Object object) throws Exception {
            List<Object> results = new ArrayList<Object>();
            BufferedReader r = new BufferedReader(new FileReader(inFile));
            skipHeader(r);
            int offset;
            for (offset = 0; offset < dataEntries; offset++) {
                results.add(DataProcessor.Persistable.dePrefix(r.readLine()));
                r.readLine();
            }
            r.close();
            return results;
        }

        public String lineAt(int l) throws IOException {
            if (l < lineCount) {
                BufferedReader r = new BufferedReader(new FileReader(inFile));
                for (int i = 0; i < l - 1; i++) r.readLine();
                String line = r.readLine();
                r.close();
                return line;
            } else {
                return null;
            }
        }

        public List<String> linesAt(int fromL, int toLine) throws IOException {
            if (toLine < lineCount && fromL < toLine) {
                List<String> results = new ArrayList<String>();
                int dist = toLine - fromL;
                BufferedReader r = new BufferedReader(new FileReader(inFile));
                for (int i = 0; i < fromL - 1; i++) r.readLine();
                while (dist-- > 0) {
                    results.add(r.readLine());
                }
                r.close();
                return results;
            } else {
                return null;
            }
        }
    }

    private static class Task {
        private final Object obj1;
        private final Object obj2;
        private final Type type;
        private final int kp;

        public static enum Type {
            Replace, Remove, New
        }

        public Task(Type type, int kp, Object obj1, Object obj2) {
            this.kp = kp;
            this.obj1 = obj1;
            this.obj2 = obj2;
            this.type = type;
        }

        public int getKp() {
            return kp;
        }

        public Object getObj1() {
            return obj1;
        }

        public Object getObj2() {
            return obj2;
        }

        public Type getType() {
            return type;
        }
    }
}
