package gitlet;

// TODO: any imports you need here

import java.io.File;
//import java.io.IOException;
import java.io.Serializable;
//import java.util.Date; // TODO: You'll likely use this in this class
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.readObject;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    static final File COMMIT_FOLDER = Utils.join(GITLET_DIR, "commits");
    private final String message;
    private final String timestamp;
    private final String  parent;//private Commit parent;
    private final Map<String,String > map;


    //private String sha1;

    /* TODO: fill in the rest of this class. */
    public Commit(String message,String timestamp,String parent,Map<String,String> map){
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.map = map;
    }

    public String  saveCommit() {
        String id;
        /*if(parent == null || map == null) {
             id =  Utils.sha1(message,timestamp);
        }
        else {
             id = Utils.sha1(message, timestamp, parent, map.toString());//Utils.sha1(this);
        }*/
        id = Utils.sha1(message, timestamp, parent, map.toString());//Utils.sha1(this);
        File newcommit = Utils.join(COMMIT_FOLDER, id);
        /*try {
            newcommit.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Utils.writeObject(newcommit,this);
        return id;
    }
   /* public void setTimestamp(String timestamp){
        this.timestamp = timestamp;

    }*/
    public String getDate(){
        return this.timestamp;
    }
    public String getMessage(){
        return this.message;
    }
   /* public  String getID(){
        return Utils.sha1(this);
    }*/
    public  Map<String, String> getMap(){
        return this.map;
    }
    public  Commit getParentcommit(){
        return getCommit(this.parent);
    }
    public String getParentID(){
        return this.parent;
    }
    public static Commit getCommit(String id){
        //sTrie
        File f = join(COMMIT_FOLDER,id);
        if(f.exists()) {
            return readObject(f, Commit.class);
        }
        return null;
    }
    public static void globallog(){
        Commit c;
        /*File[] fs = COMMIT_FOLDER.listFiles();
        for(File f:fs){
            c =  readObject(f,Commit.class);
            System.out.println("===");
            System.out.println("commit "+f.getName());
            System.out.println("Date: "+c.getDate());
            System.out.println(c.getMessage());
        }*/
        List<String> commitlist = Utils.plainFilenamesIn(COMMIT_FOLDER);
        for (String name: commitlist) {
            File f = join(COMMIT_FOLDER, name);
            c =  readObject(f,Commit.class);
            System.out.println("===");
            System.out.println("commit " + name);
            System.out.println("Date: " + c.getDate());
            System.out.println(c.getMessage());
            System.out.println("");
        }
    }
    public static void find(String message){
        Commit c;
        /*File[] fs = COMMIT_FOLDER.listFiles();
        for(File f:fs){
            c =  readObject(f,Commit.class);
            if(c.getMessage().equals(message)) {
                System.out.println(f.getName());
            }
        }*/
        Boolean nofind = true;
        List<String> commitlist = Utils.plainFilenamesIn(COMMIT_FOLDER);
        for (String name: commitlist) {
            File f = join(COMMIT_FOLDER, name);
            c =  readObject(f,Commit.class);
            if(c.getMessage().equals(message)) {
                System.out.println(name);
                nofind = false;
            }
        }
        if (nofind) {
            System.out.println("Found no commit with that message.");
        }
    }

}
