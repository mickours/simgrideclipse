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
    char * platform;
    char * deployement;
    /* check usage error and initialize with defaults */
    if (argc == 1){
        printf("** WARNING **\n using default values:\n<PROJECT_NAME>_platform.xml <PROJECT_NAME>_deployment.xml\n\n");
        char* default_args[] =
        {
            [0] = argv[0],
            [1] = "<PROJECT_NAME>_platform.xml",
            [2] = "<PROJECT_NAME>_deployment.xml"
        };
        int nb = 3;
        MSG_init(&nb, default_args);
        platform = default_args[1];
        deployement = default_args[2];
    }else if(argc == 3) {
        MSG_init(&argc, argv);
        platform = argv[1];
        deployement = argv[2];
    }else {
        printf("** ERROR **\n");
        printf("Usage:\n %s platform_file deployment_file\n", argv[0]);
        printf("Example:\n %s <PROJECT_NAME>_platform.xml <PROJECT_NAME>_deployment.xml\n", argv[0]);
        exit(1);
    }
    
    msg_error_t res = MSG_OK;
    
    /* Simulation setting */
    MSG_create_environment(platform);
    
    /* Application deployment */
    <FUNCTION_REGISTER>
    MSG_launch_application(deployement);
    
    res = MSG_main();
    XBT_INFO("Simulation time %g", MSG_get_clock());
    MSG_clean();
    if (res == MSG_OK){
        return 0;
    }else{
        return 1;
    }
} /* end_of_main */

