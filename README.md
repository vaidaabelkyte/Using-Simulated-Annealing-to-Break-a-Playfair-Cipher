<h1>Project for Artificial Intelligence (2018)</h1>
<h3><i>Using-Simulated-Annealing-to-Break-a-Playfair-Cipher</i></h3>
<h4>B.Sc. Software Development <br>
Student - Vaida Abelkyte<br>
Year - 4</h4>
<hr/>

<h3><i>Project Overview</h3>
<p>
The field of cryptanalysis is concerned with the study of ciphers, having as its objective the
identification of weaknesses within a cryptographic system that may be exploited to convert
encrypted data (cipher-text) into unencrypted data (plain-text). Whether using symmetric or
asymmetric techniques, cryptanalysis assumes no knowledge of the correct cryptographic key
or even the cryptographic algorithm being used. <br>
   <br>
Assuming that the cryptographic algorithm is known, a common approach for breaking a cipher
is to generate a large number of keys, decrypt a cipher-text with each key and then examine the
resultant plain-text. If the text looks similar to English, then the chances are that the key is a
good one. The similarity of a given piece of text to English can be computed by breaking the
text into fixed-length substrings, called n-grams, and then comparing each substring to an
existing map of n-grams and their frequency. This process does not guarantee that the outputted
answer will be the correct plain-text, but can give a good approximation that may well be the
right answer.
</p>

<h3><i>General info</h3>
<p>
The program implements the Simulated Annealing Algorithm to break the Playfair-ciphered text. <br>
The algorithm uses random changes and a heuristic function of a quad-graphs frequency sum to find a result, so sometimes it can't be found correctly, especially with short and "not English-like" text (like "iafsiojsafi"). <br>
Also, the algorithm uses initial temperature of 50, with a 0.5 cooldown per 10000 iterations. Depending on a machine performance and original text these numbers can be tweaked to achieve better results with less time. <br>
   <br>
  <b>The main architecture of the project consists of:</b> <br>
- Main class - it provides command line interface for the user. <br>
- Decoder class - it implements the heuristic function and the Simulated Annealing Algorithm itself. <br>
- Playfair class - implements a method to decode given ciphered texts with some key (encryption is not supported). <br>
- PlayfairKey class - encapsulates the Playfair matrix key data, and provides an interface for its usage (find the letter's by position, position by letter, random shuffle, etc.). <br>

<h3><i>Compile & run from a console</h3>
>javac ./src/ie/gmit/sw/ai/*.java <br>
>java -classpath "./src/" ie.gmit.sw.ai.Main

<h3><i>How to use</h3>
1. Start the application. <br>
2. Choose option "1" to load a quad-grams file and provide the path/URL to it. Wait until successful completion message appears. <br>
3. Choose option "2" to decipher a file, and provide the path/URL to the ciphered text, and where to store the output. The program will report it's actions to indicate the progress. Wait until the "Found key" message appears. <br>
4. When you want to exit the application choose option "3".

</p>


<h3><i>Snapshot of Project Requirement sheet</h3>

![pic1](https://user-images.githubusercontent.com/15648433/38314174-8f0d8bb6-381d-11e8-9bfd-82dbb5d5fc3f.png)

![pic2](https://user-images.githubusercontent.com/15648433/38314397-ff60f718-381d-11e8-9c22-b987bc39f849.png)

![pic3](https://user-images.githubusercontent.com/15648433/38314406-03fcf52e-381e-11e8-81ff-5088dc10855d.png)

![pic4](https://user-images.githubusercontent.com/15648433/38314414-094c2680-381e-11e8-96bf-3e3d799f3b41.png)



