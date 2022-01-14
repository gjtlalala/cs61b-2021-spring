package gitlet;

//import edu.princeton.cs.algs4.ST;

//import com.sun.codemodel.internal.JForEach;

import java.io.File;
//import java.sql.DataTruncation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGINGADD_DIR = join(GITLET_DIR,"stagingadd");
    //public static Map<String, String> staging_area;
    public static final File STAGINGREMOVE_DIR = join(GITLET_DIR,"stagingremove");
    public static final File OBJECT_DIR = join(GITLET_DIR,"objects");
    public static final File HEAD = join(GITLET_DIR,"HEAD");
    public static final File CURBRANCH = join(GITLET_DIR,"CURBRANCH");
    public static final File BRANCHDIR = join(GITLET_DIR,"branch");
    //private static Branch  curbranch;
    //private static LinkedList<Branch> branchlist;


    //private static LinkedList<Commit> commitslist;

    private static class Branch{
        private String name;
        private String  headid;
        Branch(String name,String headid){
            this.name = name;
            this.headid = headid;
        }
    }
    private static void gitletdirinit() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /* TODO: fill in the rest of this class. */
    public static void init(){
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        STAGINGADD_DIR.mkdir();
        STAGINGREMOVE_DIR.mkdir();
        BRANCHDIR.mkdir();
        Commit.COMMIT_FOLDER.mkdir();
        Map<String, String> map = new HashMap<>();
        //Commit commit = new Commit("initial commit","00:00:00 UTC, Thursday, 1 January 1970","0",map);
        Commit commit = new Commit("initial commit","Wed Dec 31 16:00:00 1969 -0800","0",map);
        String id = commit.saveCommit();
        setbranchhead("master",id);
        setCurbranch("master");//writeContents(CURBRANCH,"master");
        sethead(id);

    }
    /*private static void savebranch(Branch branch) {
        File f = join(BRANCHDIR,branch.name);
        writeContents(f, branch.head);
    }*/
    private static void setCurbranch(String branchname) {
        writeContents(CURBRANCH,branchname);
    }
    private static void setbranchhead(String name, String headid) {
        //Branch branch = new Branch("matser",id);
        //savebranch(branch);
        File f = join(BRANCHDIR,name);
        writeContents(f, headid);
    }
    private static void sethead(String id) {
        writeContents(HEAD,id);
        setbranchhead(getcurbranchname(),id);
    }
    private static Commit getheadcommit(){
        String id = getheadid();
        return Commit.getCommit(id);
    }
    private static String getheadid(){
         return readContentsAsString(HEAD);
    }

    private static File  getstagingaddfile(String filename){
        List<String> filelist = plainFilenamesIn(STAGINGADD_DIR);
        if(filelist == null)
            return null;
        for (String name: filelist) {
            if(name.equals(filename)) {
                File f = join(STAGINGADD_DIR,filename);
                //return readContentsAsString(f);
                return f;
            }
        }
        return null;
    }
    private static File  getstagingrmfile(String filename){
        List<String> filelist = plainFilenamesIn(STAGINGREMOVE_DIR);
        if(filelist == null)
            return null;
        for (String name: filelist) {
            if(name.equals(filename)) {
                File f = join(STAGINGREMOVE_DIR,filename);
                //return readContentsAsString(f);
                return f;
            }
        }
        return null;
    }
    private static void  createnewobject(File originfile, String fileid){
        File newfile = join(OBJECT_DIR,fileid);
        Utils.writeContents(newfile,readContents(originfile));
       // return newfile;
    }
    private static void createstageaddfile(String name ,String  fileid) {
        File newfile = join(STAGINGADD_DIR, name);
        //Utils.writeObject(newfile, blob);
        Utils.writeContents(newfile, fileid);
    }
    private static void createstageremovefile(String name ) {
        File newfile = join(STAGINGREMOVE_DIR, name);
        //Utils.writeObject(newfile, blob);
        //Utils.writeContents(newfile, fileid);
        writeContents(newfile,"0");
    }
    private static String  getcurcommitfileid(String filename) {
        Commit c = getheadcommit();
        Map<String, String> map = c.getMap();
        if(map.containsKey(filename))
            return map.get(filename);
        return null;
    }
    public static void add(String filename) {
        gitletdirinit();
        File addfile = join(CWD,filename);
        if (!addfile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        File f = getstagingrmfile(filename);
        if (f != null) {
            f.delete();//restrictedDelete(f);
            return;
        }
        String fileid = Utils.sha1(readContentsAsString(addfile));
        f = getstagingaddfile(filename);
        if (f == null || !readContentsAsString(f).equals(fileid)) {//one is not exist;one is not equal;both need new object and new stagingfie contents
            //File blob = createnewobject(addfile,fileid);
            //createnewstagefile(filename,blob);
            //System.out.println("filename "+filename +"fileid "+fileid);
            createnewobject(addfile,fileid);
            createstageaddfile(filename,fileid);
            /*f = getstagingrmfile(filename);
            if (f != null) {
                restrictedDelete(f);
            }*/

        }
        f = getstagingaddfile(filename);
        if(fileid.equals(getcurcommitfileid(filename))) {
            f.delete();
        }

    }
    /*private static File getblob(String id) {
        File f = join(OBJECT_DIR,id);
        return f;
    }*/
    public static void commit(String message){
        gitletdirinit();
        List<String> addfilelist = plainFilenamesIn(STAGINGADD_DIR);
        List<String> rmfilelist = plainFilenamesIn(STAGINGREMOVE_DIR);
        if (addfilelist.isEmpty() && rmfilelist.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit head = getheadcommit();
        String parent = getheadid();//readContentsAsString(HEAD);
        Map<String,String> filemap = head.getMap();
        for (String name: addfilelist) {
                File stagfile = join(STAGINGADD_DIR,name);
                String id = readContentsAsString(stagfile);
                //File blob = join(OBJECT_DIR, id);
                filemap.put(name, id);
                stagfile.delete();//restrictedDelete(stagfile);
        }

        if(!rmfilelist.isEmpty()) {
            for (String name : rmfilelist) {
                File stagfile = join(STAGINGREMOVE_DIR, name);
                //String id = readContentsAsString(stagfile);
                filemap.remove(name);
                stagfile.delete();//restrictedDelete(stagfile);
            }
        }

        Date date = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String currentTime = dateFormat.format(date);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        String currentTime = dateFormat.format(date);

        Commit commit = new Commit(message,currentTime,parent,filemap);
        String id = commit.saveCommit();
        sethead(id);


    }
    public static void rm(String name){
        gitletdirinit();
        Boolean error = true;
        File f = getstagingaddfile(name);
        if (getstagingaddfile(name) != null) {
            f.delete();//restrictedDelete(f);
            error = false;
        }
        Commit c = getheadcommit();
        Map<String, String> map = c.getMap();
        if(map.containsKey(name)) {
            createstageremovefile(name);
            File file = join(CWD, name);
            restrictedDelete(file);
            error = false;
        }
        if(error) {
            System.out.println("No reason to remove the file.");
        }
    }

    public static void log(){
        gitletdirinit();
        Commit c = getheadcommit();
        String id = getheadid();
        while(c != null) {
            System.out.println("===");
            System.out.println("commit "+ id);
            System.out.println("Date: "+ c.getDate());
            System.out.println(c.getMessage());
            System.out.println("");
            id = c.getParentID();
            c = c.getParentcommit();
        }
    }
    private static String getbranchheadid(String branchname) {
        File f = join(BRANCHDIR, branchname);
        return readContentsAsString(f);
    }
    public static void status(){
        gitletdirinit();
        System.out.println("=== Branches ===");
        List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
        /*(if(branchlist == null) {
            System.out.println("error branch is null");
            return;
        }*/
        String curbranchheadid = getheadid();
        for(String branchname: branchlist) {
            if(getbranchheadid(branchname).equals(curbranchheadid)) {
                System.out.println("*"+branchname);
            }
            else
                System.out.println(branchname);
        }
        System.out.println("");
        System.out.println("=== Staged Files ===");
        List<String> filelist = plainFilenamesIn(STAGINGADD_DIR);
        if(filelist != null) {
            for(String filename:filelist) {
                System.out.println(filename);
            }
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        filelist = plainFilenamesIn(STAGINGREMOVE_DIR);
        if(filelist != null) {
            for(String filename:filelist) {
                System.out.println(filename);
            }
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");
        System.out.println("=== Untracked Files ===");

    }
    private static void replaceworkingfile(String id, String name) {
        File blob = join(OBJECT_DIR, id);
        File workdir = join(CWD, name);
        writeContents(workdir, readContents(blob));
    }
    private static boolean curbranchtrackfile(String filename) {
        Commit headcommit = getheadcommit();
        Map<String ,String> curheadmap = headcommit.getMap();
        return curheadmap.containsKey(filename);
    }
    private static void checkoutcommitidallfile(String commitid){
        Commit c = Commit.getCommit(commitid);
        if(c == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Map<String, String> copyfrommap = c.getMap();
        Commit headcommit = getheadcommit();
        Map<String, String> curheadmap = headcommit.getMap();
        String branchfilename;
        for (Map.Entry<String, String> entry : copyfrommap.entrySet()) {
            branchfilename = entry.getKey();
            if(!curheadmap.containsKey(branchfilename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
           // replaceworkingfile(branchfilename,entry.getKey());
        }
        for (Map.Entry<String, String> entry : copyfrommap.entrySet()) {
            branchfilename = entry.getKey();
            replaceworkingfile(entry.getValue(),branchfilename);
        }
        for (Map.Entry<String, String> entry : curheadmap.entrySet()) {
            branchfilename = entry.getKey();
            if(!copyfrommap.containsKey(branchfilename)) {
                File f = join(CWD, branchfilename);
                restrictedDelete(f);
            }
        }
    }
    public static void checkout(String commitid,String filename,String branchname){
        gitletdirinit();
        Commit c;
        Map<String, String> copyfrommap;
        if (filename != null) {
            if(commitid == null) {
                c = getheadcommit();
            }
            else {
                //System.out.println("filename "+filename+" id "+commitid);
                c = Commit.getCommit(commitid);
                if(c == null) {
                    System.out.println("No commit with that id exists.");
                    return;
                }

            }
            copyfrommap = c.getMap();
            if(!copyfrommap.containsKey(filename)) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            if(!curbranchtrackfile(filename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
            String fileid = copyfrommap.get(filename);
            //System.out.println("blobid "+ fileid);
            replaceworkingfile(fileid,filename);
        }
        else if(branchname != null) {
            if(branchname.equals(getcurbranchname())) {
                System.out.println("No need to checkout the current branch.");
            }
            String id = getbranchheadid(branchname);
            if(id == null) {
                System.out.println("No such branch exists.");
            }
            checkoutcommitidallfile(id);
            setCurbranch(branchname);
            sethead(id);

            clearstagingarea();
        }
    }
    private static String  getcurbranchname(){
        /*List<String> filelist = plainFilenamesIn(BRANCHDIR);
        for(String name:filelist) {
            File f = join(BRANCHDIR,name);
            if(readContentsAsString(f).equals(getheadid()))
                return name;
        }*/
        return readContentsAsString(CURBRANCH);

    }
    public static void branch(String name){
        gitletdirinit();
        List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
        for(String branchname:branchlist) {
            if (branchname.equals(name)) {
                System.out.println("A branch with that name already exists.");
                return;
            }
        }
        setbranchhead(name,getheadid());
    }
    public static void rmbranch(String name){
        gitletdirinit();
        if (getcurbranchname().equals(name)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        /*List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
        for(String branchname:branchlist) {
            if (branchname.equals(name)) {
                File f = join(BRANCHDIR, name);
                restrictedDelete(f);
                return;
            }
        }*/
        File f = join(BRANCHDIR, name);
        if(!restrictedDelete(f)) {
            System.out.println("A branch with that name does not exist.");
        }
    }
    private static void clearstagingarea(){
        List<String> filelist = plainFilenamesIn(STAGINGADD_DIR);
        if (filelist == null) {
            return;
        }
        for (String name: filelist) {
            File stagfile = join(STAGINGADD_DIR,name);
            stagfile.delete();//restrictedDelete(stagfile);
        }
        filelist = plainFilenamesIn(STAGINGREMOVE_DIR);
        if (filelist == null) {
            return;
        }
        for (String name: filelist) {
            File stagfile = join(STAGINGREMOVE_DIR,name);
            stagfile.delete();//restrictedDelete(stagfile);
        }
    }
    public static void reset(String id){
        gitletdirinit();
        checkoutcommitidallfile(id);
        /*Commit c = Commit.getCommit(id);
        Map<String ,String> map = c.getMap();
        List<String>  cwdfilelist = plainFilenamesIn(CWD);
        for(String name:cwdfilelist) {
            if(!map.containsKey(name)) {
                File f = join(CWD,name);
                restrictedDelete(f);
            }
        }*/
        //setbranchhead(getcurbranchname(),id);
        clearstagingarea();
        sethead(id);
    }
    public static void merge(String name){
        gitletdirinit();

    }

}
