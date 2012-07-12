/*
 *  Generated Main Class. Most user won't have to modify it.
 */
package <PACKAGE_NAME>;
import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;
 
public class <PACKAGE_NAME>Main  {
    
    public static void main(String[] args) throws NativeException {    	
	    /* initialize the MSG simulation. Must be done before anything else (even logging). */
	    Msg.init(args);
        if(args.length < 2) {
		    Msg.info("Usage   : <PACKAGE_NAME>Main platform_file deployment_file");
            Msg.info("example : <PACKAGE_NAME>Main <PACKAGE_NAME>_platform.xml <PACKAGE_NAME>_deployment.xml");
            System.exit(1);
        }

	    /* construct the platform and deploy the application */
	    Msg.createEnvironment(args[0]);
	    Msg.deployApplication(args[1]);
	
	    /*  execute the simulation. */
	    Msg.run();
    }
}
