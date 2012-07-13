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
    MSG_init(&argc, argv);
    if (argc < 3) {
        printf("Usage: %s platform_file deployment_file\n", argv[0]);
        printf("example: %s msg_platform.xml msg_deployment.xml\n", argv[0]);
        exit(1);
    }
    
    MSG_error_t res = MSG_OK;
    
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

