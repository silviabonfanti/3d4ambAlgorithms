library(xlsx)
library(dplyr)
?dplyr::filter
# load data

data = read.csv("data.csv", sep=",")

# add some columns that will be used to compute percatenges
# nxtestType number of observations for each test type
data %>%
  group_by(testType) %>%
  mutate(nxtestType = n())
data %>%
  group_by(testType,scenario) %>%
  mutate(nxtestTypeScenario = n())

# Number of observation
nxtestType <- as.numeric(count(data[data$testType == "PEST",]))
# Number of observation for each test type and scenario
nxtestTypeScenario <- as.numeric(count(data[data$testType == "PEST" & data$scenario == 0,]))

############################
# Research question 1
#
# false negative: target 0 e FINISH_CERTIFIED
falseNeg0 <- data[data$target == 0 & data$finalResult == "FINISH_CERTIFIED",]
falseNeg <-group_by(falseNeg0, falseNeg0$testType)
summarise(falseNeg, count = n())
# false positive: target >0 e !FINISH_CERTIFIED
falsePos0 <- data[data$target > 0 & data$finalResult != "FINISH_CERTIFIED",]
falsePos <-group_by(falsePos0, falsePos0$testType)
summarise(falsePos, count = n())
# Which scenario has the highest number of false negative/false positive?
falseNeg0%>%
  group_by(scenario,testType) %>%
  summarise(count = n())
falsePos0%>%
  group_by(scenario,testType) %>%
  summarise(count = n())

# True Negative: target 0 e FINISH_CERTIFIED
trueNeg0 <- data[data$target > 0 & data$finalResult != "FINISH_CERTIFIED",]
trueNeg <-group_by(trueNeg0, trueNeg0$testType)
summarise(trueNeg, count = n())
# True Positive: target >0 e !FINISH_CERTIFIED
truePos0 <- data[data$target >= 0 & data$finalResult == "FINISH_CERTIFIED",]
truePos <-group_by(truePos0, truePos0$testType)
summarise(truePos, count = n())

# Which scenario has the highest number of true negative/true positive?
trueNeg0%>%
  group_by(scenario,testType) %>%
  summarise(count = n())
truePos0%>%
  group_by(scenario,testType) %>%
  summarise(count = n())

# Proportion test
# https://www.rdocumentation.org/packages/stats/versions/3.6.1/topics/prop.test

# RQ: Null hypothesis: The proportions of false nagative for the two populations are equal.

# Proportion for false negative
FNProp<-falseNeg0%>%
  group_by(testType) %>%
  summarise(FN = n())%>%
  mutate(nxtestType=nxtestType)
FNProp
# Apply prop.test
prop.test(FNProp$FN,FNProp$nxtestType)

#Proportion test for each scenario: False negative
#scenario 1
FNProp<-falseNeg0%>%
  filter(scenario==1)%>%
  group_by(testType) %>%
  summarise(FN = n())%>%
  mutate(nxtestType=nxtestType)
FNProp

prop.test(FNProp$FN,FNProp$nxtestType)

#scenario2
FNProp<-falseNeg0%>%
  filter(scenario==2)%>%
  group_by(testType) %>%
  summarise(FN = n())%>%
  mutate(nxtestType=nxtestType)
FNProp

prop.test(FNProp$FN,FNProp$nxtestType)


#Proportion test for each scenario: False positive
FPProp<-falsePos0%>%
  group_by(testType) %>%
  summarise(FP = n())%>%
  mutate(nxtestType=nxtestType)
FPProp

prop.test(FPProp$FP,FPProp$nxtestType)

#Proportion test for false negative
#scenario 1
FPProp<-falsePos0%>%
  filter(scenario==1)%>%
  group_by(testType) %>%
  summarise(FP = n())%>%
  mutate(nxtestType=nxtestType)
FPProp

prop.test(FPProp$FP,FPProp$nxtestType)

#scenario2
FPProp<-falsePos0%>%
  filter(scenario==2)%>%
  group_by(testType) %>%
  summarise(FP = n())%>%
  mutate(nxtestType=nxtestType)
FPProp

prop.test(FPProp$FP,FPProp$nxtestType)

############################
# Reserch question 2
# number of steps
data %>%
  group_by(testType) %>%
  summarise(mean = mean(steps))

data %>%
  group_by(scenario,testType) %>%
  summarise(mean = mean(steps))

#Wilcoxon Test
computeNullHypSteps <- function(scenario,testtype1,testtype2) {
  data1 <- data[data$scenario==scenario & data$testType == testtype1,]
  data2 <- data[data$scenario==scenario & data$testType == testtype2,]
  wilcox.test(data1$steps, data2$steps,paired = TRUE, alternative = 'greater',var.equal=TRUE)
}

computeNullHypSteps(0,"StrictStaircase","PEST3")
computeNullHypSteps(0,"StrictStaircase","PEST")
computeNullHypSteps(0,"PEST3","PEST")
computeNullHypSteps(1,"StrictStaircase","PEST3")
computeNullHypSteps(1,"StrictStaircase","PEST")
computeNullHypSteps(1,"PEST3","PEST")
computeNullHypSteps(2,"StrictStaircase","PEST3")
computeNullHypSteps(2,"StrictStaircase","PEST")
computeNullHypSteps(2,"PEST3","PEST")

############################
# Reserch question 3

 correct <- data[data$target != 0 & data$finalResult == "FINISH_CERTIFIED",]
 correct$diff = abs(correct$target - correct$level)
 
 correct%>%
   filter(diff==0) %>%
   group_by(scenario, testType) %>%
   summarise(count = n())
 

############################
# Reserch question 4
diffNOAbs <- data[data$target != 0 & data$finalResult == "FINISH_CERTIFIED",]
diffNOAbs$diff = diffNOAbs$target - diffNOAbs$level

diff01 <-diffNOAbs[diffNOAbs$diff != 0 & diffNOAbs$scenario == 1,]
tbl1 <- with(diff01, table(diff, testType))
 
diff02 <-diffNOAbs[diffNOAbs$diff != 0 & diffNOAbs$scenario == 2,]
tbl2 <- with(diff02, table(diff, testType))

opar = par(oma = c(2,0,0,0))
barplot(tbl1, beside = TRUE, col = cm.colors(17))
par(opar) # Reset par
opar =par(oma = c(0,0,0,0), mar = c(0,0,0,0), new = TRUE)
legend(x = "bottom", legend = rownames(tbl1), fill = cm.colors(17), bty = "n", ncol = 6, inset = -0.2)
par(opar) # reset par

opar = par(oma = c(2,0,0,0))
barplot(tbl2, beside = TRUE, col = cm.colors(17))
par(opar) # Reset par
opar =par(oma = c(0,0,0,0), mar = c(0,0,0,0), new = TRUE)
legend(x = "bottom", legend = rownames(tbl2), fill = cm.colors(17), bty = "n", ncol = 6, inset = -0.2)
par(opar) # reset par

myResNoAbs<-diffNOAbs%>%
filter(diff!=0) %>%
group_by(scenario, testType, diff) %>%
summarise(n())
  


############################
# Reserch question 5

# Pearson correlation test https://www.statisticshowto.datasciencecentral.com/test-retest-reliability/

computeTestRetest <- function(scenario,testtype) {
  data1 <- data[data$time==1 & data$scenario==scenario & data$testType == testtype,]
  data2 <- data[data$time==2 & data$scenario==scenario & data$testType == testtype,]
  cor.test(data1$level, data2$level, method=c("pearson", "kendall", "spearman"))
}

# scenario 0, testRetest tutto 1 uguale
computeTestRetest(0,"StrictStaircase")
computeTestRetest(0,"PEST3")
computeTestRetest(0,"PEST")

computeTestRetest(1,"StrictStaircase")
computeTestRetest(1,"PEST3")
computeTestRetest(1,"PEST")

computeTestRetest(2,"StrictStaircase")
computeTestRetest(2,"PEST3")
computeTestRetest(2,"PEST")


