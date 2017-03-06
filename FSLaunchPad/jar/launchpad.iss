; -- Example2.iss --
; Same as Example1.iss, but creates its icon in the Programs folder of the
; Start Menu instead of in a subfolder, and also creates a desktop icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=FSLaunchPad
AppVerName=FSLaunchPad version 2.34a
DefaultDirName={pf}\FSLaunchPad
DisableProgramGroupPage=yes
; ^ since no icons will be created in "{group}", we don't need the wizard
;   to ask for a group name.
DefaultGroupName=FSLaunchPad
OutputBaseFilename=setup
UninstallDisplayIcon={app}\uninstallexe.exe

[Files]
Source: "FSLaunchPad.exe"; DestDir: "{app}"
;Source: "FSLaunchPad.jar"; DestDir: "{app}"
;Source: "fslLogo.ico"; DestDir: "{app}";
Source: "data\*.*"; DestDir: "{app}/data";
;Source: "ressources\*.*"; DestDir: "{app}/ressources";
;Source: "_"; DestDir: "{app}"; Attribs: hidden; 
Source: "images\*.*"; DestDir: "{app}/images";
Source: "readme.txt"; DestDir: "{app}"; Flags: isreadme
Source: "lisezmoi.txt"; DestDir: "{app}"; Flags: isreadme

 [Dirs]
;Name: "{app}/data/_"; Attribs: hidden;

[Run]
Filename: "{app}\FSLaunchPad.exe";  WorkingDir: "{app}"; Flags: postinstall nowait skipifsilent

[Icons]
Name: "{group}\FSLaunchPad"; Filename: "{app}\FSLaunchPad.exe"; WorkingDir: "{app}"; IconFilename: "{app}\fslLogo.ico"
Name: "{userdesktop}\FSLaunchPad"; Filename: "{app}\FSLaunchPad.exe"; IconFilename: "{app}\fslLogo.ico" ; WorkingDir: "{app}"
Name: "{group}\Uninstall "; Filename: "{uninstallexe}"
;Name: "{group}\Readme "; Filename: "{app}\readme.text"
;Name: "{group}\Lisez moi "; Filename: "{app}\lisezmoi.text"


