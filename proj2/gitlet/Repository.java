package gitlet;

//import edu.princeton.cs.algs4.ST;

//import com.sun.codemodel.internal.JForEach;

//import edu.princeton.cs.algs4.ST;
//import ucb.anim.graphs.Graph;

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

    /*private static class Branch{
        private String name;
        private String  headid;
        Branch(String name,String headid){
            this.name = name;
            this.headid = headid;
        }
    }*/
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
    /*private static boolean curbranchtrackfile(String filename) {
        Commit headcommit = getheadcommit();
        Map<String ,String> curheadmap = headcommit.getMap();
        return curheadmap.containsKey(filename);
    }*/
    private static boolean fileuntrackedcurbranch(String filename) {
        Commit headcommit = getheadcommit();
        while(headcommit != null) {
            Map<String, String> curheadmap = headcommit.getMap();
            if (curheadmap.containsKey(filename)) {
                return false;
            }
            headcommit = headcommit.getParentcommit();
        }
        return true;
    }
    private static boolean fileexistcurworkingdir(String filename) {
        File f = join(CWD, filename);
        return f.exists();
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
            if (fileexistcurworkingdir(branchfilename) && fileuntrackedcurbranch(branchfilename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
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
            if (fileexistcurworkingdir(filename) && fileuntrackedcurbranch(filename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
            String fileid = copyfrommap.get(filename);
            //System.out.println("blobid "+ fileid);
            replaceworkingfile(fileid,filename);
        }
        else if (branchname != null) {
            if (branchname.equals(getcurbranchname())) {
                System.out.println("No need to checkout the current branch.");
                return;
            }
            /*List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
            if(!branchlist.contains(branchname)) {
                System.out.println("No such branch exists.");
                return;
            }*/
            if (!branchexist(branchname)) {
                System.out.println("No such branch exists.");
                return;
            }
            String id = getbranchheadid(branchname);
            checkoutcommitidallfile(id);
            setCurbranch(branchname);
            sethead(id);

            clearstagingarea();
        }
    }
    private static boolean branchexist(String branchname) {
        List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
        if(branchlist.contains(branchname)) {
           return true;
        }
        return false;
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
        /*List<String>  branchlist = plainFilenamesIn(BRANCHDIR);
        if(!branchlist.contains(name)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }*/
        if (!branchexist(name)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        File f = join(BRANCHDIR, name);
        f.delete();
    }
    private static void clearstagingarea(){
        List<String> filelist = plainFilenamesIn(STAGINGADD_DIR);
        if (filelist != null) {
            for (String name : filelist) {
                File stagfile = join(STAGINGADD_DIR, name);
                stagfile.delete();//restrictedDelete(stagfile);
            }
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
    private static boolean stagingareahasfile() {
        List<String> filelist = plainFilenamesIn(STAGINGADD_DIR);
        if ( filelist.isEmpty()) {
            return false;
        }
        filelist = plainFilenamesIn(STAGINGREMOVE_DIR);
        if (filelist.isEmpty()) {
            return false;
        }
        return true;
    }
    /*private static boolean mergenecessary(String filename) {

    }
    private static boolean hasconflict(String filename) {

    }*/
    private static String getsplitid(Commit mergebranch, Commit curbranch) {
        HashSet<String> set = new HashSet<>();
        String spiltid = null;
        while(curbranch != null && mergebranch != null) {
            if(!set.contains(curbranch.getParentID())) {
                set.add(curbranch.getParentID());
                curbranch = curbranch.getParentcommit();
            }
            else {
                spiltid = curbranch.getParentID();
                break;
            }
            if(!set.contains(mergebranch.getParentID())) {
                set.add(mergebranch.getParentID());
                mergebranch = mergebranch.getParentcommit();
            }
            else {
                spiltid = mergebranch.getParentID();
                break;
            }
        }
        return spiltid;
    }
    private static void mergeconflict(String filename,String curid, String mergefileid) {
        System.out.println("Encountered a merge conflict.");
        File workfile = join(CWD,filename);
        String m = "<<<<<<< HEAD\n";
        if(curid != null) {
            File curfile = join(OBJECT_DIR, curid);
            m += readContentsAsString(curfile);
        }
        m +=  "=======\n";
        if(mergefileid != null) {
            File mergefile = join(OBJECT_DIR, mergefileid);
            m += readContentsAsString(mergefile);
        }
        m += ">>>>>>>";
        writeContents(workfile,m);
        String fileid = Utils.sha1(m);
        createnewobject(workfile,fileid);
        createstageaddfile(filename,fileid);
    }
    public static void merge(String branchname){
        gitletdirinit();
        if (stagingareahasfile()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!branchexist(branchname)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchname.equals(getcurbranchname())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit curbranchhead = getheadcommit();
        String mergebranchheadid = getbranchheadid(branchname);
        Commit mergebranchhead = Commit.getCommit(mergebranchheadid);
        String spiltid = getsplitid(mergebranchhead,curbranchhead);
        if (spiltid == null) {
            System.out.println("ERROR,cannot find the split id");
            System.exit(0);
        }
        if (spiltid.equals(mergebranchheadid)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        Commit splitcommit = Commit.getCommit(spiltid);
        Map<String, String> splitmap = splitcommit.getMap();
        Map<String, String> curmap = curbranchhead.getMap();
        Map<String, String> mergemap = mergebranchhead.getMap();
       // List<String>  workinglist = plainFilenamesIn(CWD);
        for (Map.Entry<String, String> entry : splitmap.entrySet()) {//split  exist
            String filename = entry.getKey();
            String splitid = entry.getValue();
            String curid = curmap.get(filename);
            String mergefileid = mergemap.get(filename);//maybe null
            if(curid != null) { // cur branch exist
                if (splitid.equals(curid)) {//cur branch not modify
                    if (mergefileid == null) { //merge branch not exist
                        File f = join(CWD, filename);
                        f.delete();
                        createstageremovefile(filename);
                    } else if (!splitid.equals(mergefileid)) {//merge branch modify
                        replaceworkingfile(mergefileid, filename);
                        createstageaddfile(filename, mergefileid);
                    }
                }
                else if(mergefileid == null) {
                    //1.cur branch modify ,mergefile unexist
                    System.out.println("11111");
                    mergeconflict(filename, curid, mergefileid);
                }
                else if (!mergefileid.equals(spiltid) && !mergefileid.equals(curid)){
                    //2. cur branch modify ,merge exist modify and not equal split , files modified in different ways in the current and given branches
                    System.out.println("222");
                    mergeconflict(filename, curid, mergefileid);
                }
            }
            else if(mergefileid != null && !mergefileid.equals(spiltid)) {
                //cur branch no exist ,mergefile exist and modifyed
                System.out.println("333");
                mergeconflict(filename,curid,mergefileid);
            }
        }
        for (Map.Entry<String, String> entry : mergemap.entrySet()) {
            String filename = entry.getKey();// merge branch exist
            if(!splitmap.containsKey(filename) ) {//split not exist
                String mergefileid = mergemap.get(filename);
                if(!curmap.containsKey(filename)) { //cur branch not exist
                    replaceworkingfile(mergefileid, filename);
                    createstageaddfile(filename, mergefileid);
                }
                else if(!curmap.get(filename).equals(mergefileid)) {//cur branch exist ,not equal to merge id
                    System.out.println("444");
                    mergeconflict(filename,curmap.get(filename),mergefileid);
                }
            }

        }
        commit("Merged "+ branchname + " into "+getcurbranchname()+".");
    }

}
