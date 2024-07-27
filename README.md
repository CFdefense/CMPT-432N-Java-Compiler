<h2 align="center">6502 Microprocessor Compiler That Translates High-Level Code into Executable 6502 Microprocessor Machine Code</h3>  
 <h3 align="center">Project Information</h4>
 <p align="center">
  I started and completed this project during my free time while interning at Avangrid Networks in the summer of 2024. During my freshman year, my upperclassmen friends could not stop talking about this class and the hardships it brought them. As such I thought I would give it my best and try to push myself to complete the class independently of any teaching. Originally this class is taught during the junior or senior year of a CS degree at Marist College. It is titled 'Design of Compilers' and is taught in person by Professor Labouseur. I want to give a big shout-out to Alan Labouseur who provided all of the materials and study information I needed to climb the mountain that was this project free of charge on <a href="https://www.labouseur.com/courses/compilers/"><strong>his website</strong></a>
  <br />
  <br />
My goal when starting this project was to gain a deeper knowledge and skill with Java through a large-scale and difficult project. This compiler is currently by far my largest project and has consumed many many hours of my life, however, as a result, I can say this project is the one I am most proud of. I created this functional 6502 microprocessor compiler through hours of headache, self-study, and determination. 
 </p>
  <h3 align="center">Project Overview and Limitations</h3> 
  <p align="center">
  This Project is a 6502 Microprocessor Compiler, the 6502 microprocessor is an 8-bit microprocessor that was widely used in the late 1970s and 1980s. Developed by MOS Technology, it became one of the most popular CPUs for home computers, video game consoles, and embedded systems. The 6502 Microprocessor is most notably the processor used in the Atari 2600 and Nintendo NES. The purpose of a Compiler is to translate high-level language that is easy for developers to understand but hard for machines to understand into machine code that the machine (in our case the 6502 microprocessor) can better understands and execute. 
  <br />
  <br />
   This project is split into 4 unique sub-projects, each a segment of the compiler that builds on the last segments. The final project is the FINAL complete version of the compiler. This setup was very intuitive and made each aspect of the compiler easy to comprehend while still challenging to implement. Along with each sub-project, I assembled my own set of test programs to test various edge cases and functionality. Each test has a unique purpose indicated by its name and were designed to check both the continued successful functionality of previous sub-projects and the current one being built.
   <br />
   <br />
   Due to the complexity of a Compiler and the vast amount of differing ways in which we can write high-level language, it becomes necessary for the sake of realistic expectations to set limitations on ourselves for this project. These limitations will help us create an intuitive compiler that will be both amazing and practical in the sense that it can be reasonable created in a few months time. To create this perfect balance, Professor Labouseur restricted the acceptable high-level grammar that this compiler will allow for use. <a href="https://www.labouseur.com/courses/compilers/grammar.pdf"><strong>See Acceptable Grammer Here</strong></a>
  </p>
  <h3 align="center">Lets Dive Into The Compiler!</h3> 
  <p align="center">
  <h4 align="center">Project One: Lexical Analysis</h4> 
  <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project1.pdf">Project One</a> starts the compiler off with the lexer, the lexer's job is to run lexical analysis which is the first check our high-level language will have to pass. Lexical analysis involves checking every single character written in our high-level language for the creation of a token stream. A token can be a word such as <strong>string</strong> or <strong>int</strong> or a character such as <strong>a</strong> or <strong>{</strong> Checking means to compare the words and characters in our high-level language to our acceptable grammar. This might seem easy, however, how can this be done effectively? and how will we differentiate between a character and a word? The solution to this question is the main challenge of this first project. This can be solved using regular expressions aka Regex. Regex is a powerful and efficient import that can be used for pattern recognition, ie it will recognize an <strong>I</strong> followed by an <strong>N</strong> and then a <strong>T</strong> as INT instead of each individual character. Using this tool we read through our imported high-level language and can form accurate tokens. Regex also has another great purpose in identifying bad characters, in such an event we can throw errors to the terminal for changes to be made in the high-level language. Following the completion of this project will we have a functioning lexer that can correctly convert high-level language into a stream of useful tokens, ignoring spaces and throwing errors for any unwanted symbols. We are now ready to move on to parsing.
  </p>
  <h4 align="center">Project Two: Parsing </h4> 
   <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project2.pdf">Project Two</a> 
  </p>
  <h4 align="center">Project Three: Semantic Analysis</h4> 
  <h4 align="center">Project Four: Code Generation</h4> 
  </p>
</div>

## Self-Taught and COMPLETED 7/22/2024

## LaTeX Writeups and Some Code Adjustments May be Required


