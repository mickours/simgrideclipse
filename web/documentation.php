<h1>Documentation</h1>
<?php TableOfContents(3,__FILE__); ?>

<h2><a name="Installation">Installation</a></h2>
<p>see <a href="./install.html">Installation page</a></p>

<h2><a name="Features overview">Features overview</a></h2>
<p>
SimGrid eclipse plug-in adds the following features to Eclipse: 
<ul>
<li>A <b>SimGrid MSG project Wizard</b> that creates for you all files needed to start a MSG simulation, in both C and Java.</li>
<li>A <b>SimGrid Platform Visual Editor</b> for platform files that helps you to visualize and edit your platform files.</li>
</ul>
</p>


<h2><a name="SimGrid MSG project Wizard">SimGrid MSG project Wizard</a></h2>
<p>
There is actually two supported languages: C witch is the native SimGrid language, and Java through the <a href="http://simgrid.gforge.inria.fr/simgrid-java/3.7.1/doc/">Java-Bindings</a>.</p>
<p>
Here are the steps to create a SimGrid project:
<ol>
<li>click on <b>File > New > Project... (Ctr+N)</b></li>
<li>select the <b>SimGrid</b> category</li>
<li><b>select</b> the project for your favorite <b>language</b></li>
<li>enter your <b>project name</b></li>
<li>at this step <b>IF you have install SimGrid properly (see <a href="./install.html">Installation page</a>)</b></li>
    <ul>
        <li>you could (see <b>WARNINGS</b>) click on <b>finish</b> and you have your default new project</li>
        <li><b>OR</b> you could click <b>next</b> and set <b>Custom Parameters</b></li>
    </ul>
</ol>
</p>

<div class="warning">
<h3>WARNINGS</h3>
<ul>
    <li>if you choose the <b>SimGrid MSG C Project</b> and you are using SimGrid 3.8 or later you <b>MUST</b> go to <b>Custom Parameters</b> page and check the button that ask this</li>
    <li>it is <b>NOT RECOMMENDED to chose manually the libraries paths</b> that are required, they should be taken from the environments variables (see <a href="./install.html">Installation page</a>)</li>
</ul>
</div>


<h3><a name="SimGrid MSG project Wizard.Custom Parameters">Custom Parameters</a></h3>
<p>You can give your functions names to fill the generated files properly. If you don't, one default function named "defaultFunction" is created to give you an example.
</p>

<h3><a name="SimGrid MSG project Wizard.Deployment files">Deployment files</a></h3>
<p>When you create a SimGrid MSG project a deployement file is created. This contains only a little template to show you how to use it. If you want to link a function to an host, you have to set the "host" field with your host id and the "function" field with your function name.
</p>

<div class="warning">
<h3>WARNINGS</h3>
<p>the name of your function that you have to put in the "function" field must be have <b>no "()" at the end</b>, and depends on the laguage you are using:
<ul>
    <li>Java -> YourPackageName<b>.</b>YourFunctionName</li>
    <li>C -> YourFunctionName</li>
</ul>
</p>
</div>

<h2><a name="Platform Visual Edition">Platform Visual Edition</a></h2>
<p>
As the platform files are in XML format, you need to specify to Eclipse that you want to open it with the <b>SimGrid Visual Editor</b>. To use it instead of the normal XML editor given by Eclipse, all you have to do is to <b>right-click on your platform description file</b> and to select <b>open With > SimGrid Editor</b>

The <b>SimGrid Visual Editor</b> is composed of:
<ul>
    <li>The <b>palette</b> that contains the tools to <b>create and link element</b> for your own platform. To create an element, select a tool and click on the editing zone to create your element.
    </li>
    <li>The <b>editing zone</b> where you can <b>move, remove and edit</b> the elements
    </li>
    <li>The <b>Outline View</b> can help you to navigate inside your platform.<br/>
    You can access to it in the menu <b>Window > Show View > Outline</b>
    </li>
    <li>The <b>Text Editor</b> is accessible from the bottom tabs. It is fully synchronize with the visual editor and allows you to edit "Ruled Base" routing AS for example</li>
</ul>

<div class="warning">
<h3>WARNINGS</h3>
<ul>
<li>when an AS has a <b>"Rule Based", "None" or "Cluster" routing, the routes are not shown</b></li>
<li>the elements positions and the default values are session-wide: if you quit Eclipse you loose them</li>
</ul>
</p>
</div>


<h3><a name="Platform Visual Edition.How to use the SimGrid Editor and Tips">How to use the SimGrid Editor and Tips</a></h3>
<ul>
<li><b>open an AS</b> with a <b>double-click</b> or a <b>right-click > Go Into</b> on it</li>
<li>right-click on an AS or in the background (when nothing is selected) and you can <b>set the AS routing directly</b></li>
<li><b>double-click (or right-click > Edit...)</b> on an other element allow you <b>to edit</b> this element</li>
<li>you can <b>move a route</b> that from an element to another by <b>dragging one extremity and drop it</b> on the other element</li>
<li>you can <b>Zoom in/out using Ctr+=/Ctr+- or Ctr+mouse wheel</b></li>
<!--<li>You can set session-wide default values for your element creations</li>-->
<li>if your not happy with the first layout, you can use the <b>Auto Layout action in the SimGrid Menu</b> to perform new one</li>
<li>you can see and edit to all the <b>properties of the selected element</b> with <b>right-click on the Text Editor and select 
Properties</b></ul>


<h3><a name="Platform Visual Edition.Import an existing platform">Import an existing platform</a></h3>
<div class="warning">
<h3>WARNINGS</h3>
<p>
Eclipse need a project to put your platform into so if you already have one you can skip the directly to the step 5
</p>
</div>
<p>
You want to explore an existing file?<br/>
Here is a simple way to do this:
<ol>
<li>click on <b>File > New > Project...</b></li>
<li>select the <b>"General"</b> category</li>
<li>select <b>"Project"</b> and click <b>next</b></li>
<li><b>give a name</b> to your project</li>
<li>right-click on your new Project and select <b>Import...</b></li>
<li>select the <b>"General"</b> category</li>
<li>select <b>"File System"</b> and click <b>Next</b></li>
<li><b>choose the containing folder</b> of your platform</li>
<li><b>select the platform(s)</b> that you want to import and click <b>Finish</b></li>
<li>right-click on your platform file and select <b>Open With > SimGrid Editor</b></li>
</ol>
</p>
