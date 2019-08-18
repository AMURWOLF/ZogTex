# ZogTex
Eclipse plug-ins for LaTeX writers

## How to import and run in Eclipse IDE.

Tested on Eclipse IDE for RCP and RAP Developers Version: 2019-06 (4.12.0).

1) Import all plugins into workspace as plugin projects.
2) Create new Eclipse Application run/debug configuration: 
  - Run a product "org.eclipse.platform.ide"
  - Set at tab "Plug-ins" enable autostart (true) by default. 
  - Choose start level "5" or higher for plugin "com.zog.tex.bib.model". 
    Default start level is "4".
  - Enable only required plug-ins.
3) Launch run/debug.

Also, you can launch Junit Plug-in Test:
  - Select run all tests in project "com.zog.tex.integr.tests".
  - Select Junit4.
