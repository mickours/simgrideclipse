#include <stdio.h>
#include "msg/msg.h"
#include "xbt/sysdep.h"

/* Create a log channel to have nice outputs. */
#include "xbt/log.h"
#include "xbt/asserts.h"
XBT_LOG_NEW_DEFAULT_CATEGORY(msg_<PROJECT_NAME>,"Messages specific for this simulation");

<FUNCTIONS>

int main(int argc, char *argv[])
{
    /* check usage error and initialize with defaults */
    if (argc == 1){
        printf("** WARNING **\n using default values:\n<PROJECT_NAME>_platform.xml <PROJECT_NAME>_deployment.xml\n\n");
        argv[1] = "<PROJECT_NAME>_platform.xml";
        argv[2] = "<PROJECT_NAME>_deployment.xml";
    }else if(argc != 3) {
        printf("** ERROR **\n");
        printf("Usage:\n %s platform_file deployment_file\n", argv[0]);
        printf("Example:\n %s <PROJECT_NAME>_platform.xml <PROJECT_NAME>_deployment.xml\n", argv[0]);
        exit(1);
    }
    
    MSG_init(&argc, argv);
    
    msg_error_t res = MSG_OK;
    
    /* Simulation setting */
    MSG_create_environment(argv[1]);
    
    /* Application deployment */
    <FUNCTION_REGISTER>
    MSG_launch_application(argv[2]);
    
    res = MSG_main();
    XBT_INFO("Simulation time %g", MSG_get_clock());
    MSG_clean();
    if (res == MSG_OK){
        return 0;
    }else{
        return 1;
    }
} /* end_of_main */

