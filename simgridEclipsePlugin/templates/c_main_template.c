#include <stdio.h>
#include "msg/msg.h"
#include "xbt/sysdep.h"

/* Create a log channel to have nice outputs. */
#include "xbt/log.h"
#include "xbt/asserts.h"
XBT_LOG_NEW_DEFAULT_CATEGORY(msg_<PROJECT_NAME>,"Messages specific for this msg example");
int sender(int argc, char *argv[]);
int receiver(int argc, char *argv[]);
msg_error_t test_all(const char *platform_file,
const char *application_file);

<FUNCTIONS>

int main(int argc, char *argv[])
{
    msg_error_t res = MSG_OK;
    MSG_init(&argc, argv);
    if (argc < 3) {
        printf("Usage: %s platform_file deployment_file\n", argv[0]);
        printf("example: %s msg_platform.xml msg_deployment.xml\n", argv[0]);
        exit(1);
    }
    msg_error_t res = MSG_OK;
    
    /* Simulation setting */
    MSG_create_environment(platform_file);
    
    /* Application deployment */
    <FUNCTION_REGISTER>
    MSG_launch_application(application_file);
    
    res = MSG_main();
    XBT_INFO("Simulation time %g", MSG_get_clock());
    MSG_clean();
    if (res == MSG_OK){
        return 0;
    }else{
        return 1;
    }
} /* end_of_main */

