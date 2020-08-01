##-XX:+UseG1GC VS -XX:+UseParallelGC

### -Xms256m -Xmx256m -XX:+UseG1GC
   
  |GC Usage Times      | Time Before OutOfMemory | Total Stop The World Time |Throutput loop  |
  |--------------------|---------------------    |---------------------------|----------------|
  |     17             | 521                     | 328                       |   2            |
  
### -Xms256m -Xmx256m -XX:+UseParallelGC
 
  |GC Usage Times      | Time Before OutOfMemory | Total Stop The World Time | Throutput loop |
  |--------------------|---------------------    |---------------------------|----------------|
  |     5              | 1200                    | 1100                      |   1            |
  
  
### -Xms2048m -Xmx2048m -XX:+UseG1GC
   
  |GC Usage Times      | Time Before OutOfMemory | Total Stop The World Time  |Throutput loop |
  |--------------------|---------------------    |---------------------------|----------------|
  |     150            |  19526                  | 11231                     |   99           |
  
  
### -Xms2048m -Xmx2048m -XX:+UseParallelGC
 
  |GC Usage Times      | Time Before OutOfMemory | Total Stop The World Time | Throutput loop |
  |--------------------|---------------------    |---------------------------|----------------|
  |     39           | 62481                     | 10305                     |  71            |
  
### -Xms4096m -Xmx4096m -XX:+UseG1GC
   
  |GC Usage Times      | Time Before OutOfMemory  | Total Stop The World Time |Throutput loop  |
  |--------------------|---------------------     |---------------------------|----------------|
  |     163            |  117488                  | 27238                     |   204          | 
  
  
### -Xms4096m -Xmx4096m -XX:+UseParallelGC
 
  |GC Usage Times      | Time Before OutOfMemory  | Total Stop The World Time | Throutput loop |
  |--------------------|---------------------     |---------------------------|----------------|
  |     45             | 47586                    | 31617                     |  169           | 
  
### -Xms8192m -Xmx8192m -XX:+UseG1GC
   
  |GC Usage Times      | Time Before OutOfMemory  | Total Stop The World Time  |Throutput loop |
  |--------------------|---------------------     |---------------------------|----------------|
  |     184            |  107510                  | 58271                     |   414          | 
  
  
### -Xms8192m -Xmx8192m -XX:+UseParallelGC
 
  |GC Usage Times      | Time Before OutOfMemory  | Total Stop The World Time | Throutput loop |
  |--------------------|---------------------     |---------------------------|--------------- |
  |     48             | 180599                   | 61946                     |  358           | 
  
  
##Выводы
`По данным, хорошо видно, что G1 GC обеспечивает меньшую задержку времени выполнения кода (число проходов по циклу выше),
Но он намного быстрее падает по OutOfMemory. 
Так же видно, что при использовании Parallel GC приложение останавливается реже, задержка времени выполнения кода - выше, но
приложение работает дольше до падения в OutOfMemory`