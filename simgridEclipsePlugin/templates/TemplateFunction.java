/*
 * Generated function Class.
 */
package <PACKAGE_NAME>;
import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.Process;

public class <FUNCTION_NAME> extends Process {
   public <FUNCTION_NAME>(Host host, String name, String[]args) {
		super(host,name,args);
   } 
    
   public void main(String[] args) throws MsgException {
        
      Msg.info("<FUNCTION_NAME> started");

	// INSERT YOUR FUNCTION CODE HERE            

      Msg.info("<FUNCTION_NAME> exit");
    }
}
