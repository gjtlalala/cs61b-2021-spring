package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {
    public static void hastwoarg(String[] args){
        if (args.length != 2) {
            Utils.error("Incorrect operands.");
        }
    }

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
            //Utils.error("Please enter a command.");
        }

        // TODO: what if args is empty?
        String firstArg = args[0];
        String message;
        String name;
        String id;
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                hastwoarg(args);
                name = args[1];

                Repository.add(name);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                hastwoarg(args);
                message = args[1];
                if (message.equals("")) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(message);
                break;
            case "rm":
                hastwoarg(args);
                name = args[1];
                if (name == null) {
                    Utils.error("Please enter a rm file name.");
                }
                Repository.rm(name);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Commit.globallog();
                break;
            case "find":
                hastwoarg(args);
                message = args[1];
                Commit.find(message);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if (args.length == 2) {
                    name = args[1];
                    Repository.checkout(null,null,name);
                }
                if (args.length == 3) {
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                        //Utils.error("Incorrect operands.");
                    }
                    name = args[2];
                    Repository.checkout(null,name,null);
                }
                if (args.length == 4) {
                    id = args[1];
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                        //Utils.error("Incorrect operands.");
                    }
                    name = args[3];
                    Repository.checkout(id,name,null);
                }
                break;
            case "branch":
                hastwoarg(args);
                name = args[1];
                if (name == null) {
                    //Utils.error("Please enter a branch name.");
                    System.out.println("Please enter a branch name.");
                    System.exit(0);

                }
                Repository.branch(name);
                break;
            case "rm-branch":
                hastwoarg(args);
                name = args[1];
                if (name == null) {
                    //Utils.error("Please enter a branch name.");
                    System.out.println("Please enter a branch name.");
                    System.exit(0);
                }
                Repository.rmbranch(name);
                break;
            case "reset":
                hastwoarg(args);
                id = args[1];
                if (id == null) {
                    //Utils.error("Please enter an id.");
                    System.out.println("Please enter an id.");
                    System.exit(0);
                }
                Repository.reset(id);
                break;
            case "merge":
                hastwoarg(args);
                name = args[1];
                if (name == null) {
                    //Utils.error("Please enter a branch name.");
                    System.out.println("Please enter a branch name.");
                    System.exit(0);
                }
                Repository.merge(name);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                //Utils.error("No command with that name exists.");
                break;
        }
    }
}
