# Speaking-rate-and-Pitch-Based-System-Actuation
- Calculated Speaking Rate and Pitch through in an Android smartphone. 
- Microcontroller used for physical system actuation.
- 
->  Project work related to Speech Signal Processing.
->  Speaking rate is the number of syllables spoken per second.
->  In Layman’s terms, faster we speak, more the speaking rate and vice versa.
So basically, if we want to analyze the signal and process it for Speech Signal Processing or if we want to know the type of speech we calculate the speaking rate, apart from that, we can also use it to understand the process of Speech Signal processing by interfacing the calculation of Speaking rate and actuate some physical system according to the Speaking rate, i.e. If we have high speaking rate to low speaking rate, some system can be actuated in order to understand how and what exactly is Speaking rate and what are the paths that speech signal take in order to perform the given action.
->  Speaking Rate means number of syllables per second, or in a more general sense, number of vowels per second. As vowels contain the peak amount of energy present in a speech signal, they are used for calculating the Speaking rate of a speech audio input.

How did I calculated Speaking rate ?
->  Speaking rate is the number of syllables spoken per second.
->  So basically, for this first of all, we needed a signal.
->  The signal was sampled at 16000 Hz, meaning 16000 samples obtained per second.
->  All this work was done in Android Studio in the form of an Application in programming language “Java”.
->  From those, I took 320 samples at a time, calculated their signal energy, stored that value in a frame.
     Now, this frame comprised of 320÷16000=20 milliseconds.
     Then I shifted by 160 samples and took the next 320 samples and so on until 16000 samples.
     So, ideally I should take 50 such frames for adding up to total 1 second, but as I shifted and did the windowing over half the original samples(320) so I had to take double frames, i.e. 100 frames. So these 100 frames comprised of 1 second total energy.
->  Now, we got the total energy for 1 second, which is duration above which we need to calculate the speaking rate. After that, filtering was required so that unnecessary peaks can be removed.
->  So, I applied low pass 2nd Order Butterworth Filter. As we all know about the Butterworth filter, I got the coefficients A₀,A₁,A₂ and B₀,B₁,B₂ from the Matlab command “butter”.
->  After obtaining those values, I created the equation :                                              
               y(n+2)=0.0366∗x(n+2)+0.0731 ∗x(n+1)+0.0366∗x(n)+1.3909∗y(n+1)−0.5372∗y(n),
where y is output and x is input.
->  So applying the Butterworth filtered 2nd order low pass equation, what we got was this output, where red represents filtered output and blue indicated input 100 frames’ energy values.
->  Now, after obtaining the filtered output, a simple Thresholding was done to remove the noises, which doesn’t account in the Speaking rate calculation contribution.
->  After that, only thing left was calculation of peaks among those 100 frames filtered output, with minimum peak distance of 18-20.
->  To calculate the peaks, which were going to be termed as Speaking rate after this procedure, I took the difference of next value and present value. If it was positive, meaning increasing curve, flag 1 was set else, flag -1 was set meaning decreasing curve. Now for local maxima(peaks), this pattern was needed was (1,-1) as this pattern would correspond to a local maxima. So this pattern was applied and local maxima were found.

Procudure and Implementation:
->  The speaking rate is then used to actuate a physical system, the system being: A ball has been kept above a 2-way DC Motor which has been kept inside a long vertical cylindrical pipe. Now, our Android Smart phone application calculating speaking rate and pitch is connected to WiFi module at any meagre amount of distance, which in turn is connected to microcontroller. Microcontroller is connected to a motor driver L293D, which is forwarded to 2-way DC Motor. Here Mapping of values is done in this order:
          -  0-10 (Speaking rate values)
          -  0-255 (8-bit values of microcontroller) -
          -  0-5 (Corresponding Voltage values of Microcontroller for governing respective task) 
          -  0-12 (Corresponding Voltage values of Motor obtained after passing through a motor driver for governing respective task)
->  Finally, after obtaining the corresponding voltage values of 0-12 V, the speed of DC Motor changes accordingly and hence as the speaking rate changes accordingly the ball inside the vertical pipe will be going up-down(change it's relative position).

Adding Pitch and Finalizing the Application:
->  A pitch detection algorithm (PDA) is an algorithm designed to estimate the pitch or fundamental frequency of a virtually periodic signal, usually a digital recording of speech or a musical note or tone.
->  The YIN algorithm was used to calculate the pitch in this application which directly gives us the pitch in float values.So, following processes were going on inside the Android application:
       Starting the recording(taking input signal)
       Calculating the Speaking Rate
       Calculating the Pitch
       Playing the recorded sound
       Displaying Time, Speaking Rate and Pitch in real-time on Android Smart Phone
       Three buttons were kept for : Connecting to Wi-Fi Network
                                                          : Sending the Speaking Rate to the microcontroller for further physical system actuation                                                             : Sending the Pitch to the microcontroller for further physical system actuation.
->  And the Pitch of the sound was calculated and corrrespondingly the LCD System was made consisting of Bar Graph Representation in 1st Row and "Pitch Analyser" written in 2nd Row. Thus, corresponding to different values of Pitch of the sound, the Bar Graph changed accordingly.

































 ->  Project led under Professor Prasanta Kumar Ghosh at his laboratory SPIRE.
To visit the page of SPIRE: Click Here

Code:
- To view the code, click on GitHub Icon below:
