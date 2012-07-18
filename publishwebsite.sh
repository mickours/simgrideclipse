#!/bin/bash
ssh scm.forge.imag.fr 'rm -fr /home/groups/simgrideclipse/htdocs/* '
scp -r web/* scm.forge.imag.fr:/home/groups/simgrideclipse/htdocs/
ssh scm.forge.imag.fr 'chmod -R 775 /home/groups/simgrideclipse/htdocs/* '

