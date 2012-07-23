/*
 *  Generated Main Class. Most user won't have to modify it.
 */
package <PACKAGE_NAME>;
import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;
 
public class <PACKAGE_NAME>Main  {
    
    public static void main(String[] args) throws NativeException {    
        /* check usage error and initialize with defaults */
        if (args.length == 0){
            args = new String[2];
            System.out.print("** WARNING **\nusing default values:\n"+
                "<PACKAGE_NAME>_platform.xml <PACKAGE_NAME>_deployment.xml\n\n");
            args[0] = "<PACKAGE_NAME>_platform.xml";
            args[1] = "<PACKAGE_NAME>_deployment.xml";
        }else if(args.length != 2) {
            System.out.print("** ERROR **\n"+
                "Usage:\nplatform_file deployment_file\n");
            System.out.print("Example:\n<PACKAGE_NAME>_platform.xml <PACKAGE_NAME>_deployment.xml\n");
            System.exit(1);
        }	
	    /* initialize the MSG simulation. Must be done before anything else (even logging). */
        Msg.init(args);
        Msg.info("Simulation start...");
	    /* construct the platform and deploy the application */
        Msg.createEnvironment(args[0]);
        Msg.deployApplication(args[1]);
	    
	    /*  execute the simulation. */
        Msg.run();
        Msg.info("Simulation time:"+Msg.getClock());
    }
}
