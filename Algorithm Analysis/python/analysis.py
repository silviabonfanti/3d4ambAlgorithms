# %% 

import numpy as  np
import pandas as pd
import csv
from scipy.stats import chi2_contingency

datas0=pd.read_csv("Tests0.csv", quoting=csv.QUOTE_NONE)
datas1=pd.read_csv("Tests1.csv", quoting=csv.QUOTE_NONE)
datas2=pd.read_csv("Tests2.csv", quoting=csv.QUOTE_NONE)
dataatt=pd.read_csv("TestAttention.csv", quoting=csv.QUOTE_NONE)

'''
############################
# Research question 1
#
# false negative: target 0 e FINISH_CERTIFIED
# false positive: target >0 e !FINISH_CERTIFIED
filterS0FN = (datas0["FinalRes"]=="FINISH_CERTIFIED") & (datas0["Target"]==0)
filterS0FP = (datas0["FinalRes"]!="FINISH_CERTIFIED") & (datas0["Target"]>0)
countfn = datas0["Target"][filterS0FN].count()
countfp = datas0["Target"][filterS0FP].count()
print("False negative S0 " +  str(countfn))
print("False positive S0 " +  str(countfp))

filterS1FN = (datas1["FinalRes"]=="FINISH_CERTIFIED") & (datas1["Target"]==0)
filterS1FP = (datas1["FinalRes"]!="FINISH_CERTIFIED") & (datas1["Target"]>0)
countfn = datas1["Target"][filterS1FN].count()
countfp = datas1["Target"][filterS1FP].count()
print("False negative S1 " + str(countfn))
print("False positive S1 " +  str(countfp))

filterS2FN = (datas2["FinalRes"]=="FINISH_CERTIFIED") & (datas2["Target"]==0)
filterS2FP = (datas2["FinalRes"]!="FINISH_CERTIFIED") & (datas2["Target"]>0)
countfn = datas2["Target"][filterS2FN].count()
countfp = datas2["Target"][filterS2FP].count()
print("False negative S2 " + str(countfn))
print("False positive S2 " +  str(countfp))

filterAttFN = (dataatt["FinalRes"]=="FINISH_CERTIFIED") & (dataatt["Target"]==0)
filterAttFP = (dataatt["FinalRes"]!="FINISH_CERTIFIED") & (dataatt["Target"]>0)
countfn = dataatt["Target"][filterAttFN].count()
countfp = dataatt["Target"][filterAttFP].count()
print("False negative Attenzione " + str(countfn))
print("False positive Attenzione " +  str(countfp))

# false negative: target 0 e FINISH_CERTIFIED -> GROUPED BY TEST TYPE
print("FALSE NEGATIVE GROUPED BY TEST TYPE")
S0FN=(filterS0FN).groupby(datas0['TestType']).sum()
print("S0")
print(S0FN)
S1FN=(filterS1FN).groupby(datas1['TestType']).sum()
print("S1")
print(S1FN)
S2FN=(filterS2FN).groupby(datas2['TestType']).sum()
print("S2")
print(S2FN)
AttFN=(filterAttFN).groupby(dataatt['TestType']).sum()
print("Att")
print(AttFN)

# false positive: target >0 e !FINISH_CERTIFIED -> GROUPED BY TEST TYPE
print("FALSE POSITIVE GROUPED BY TEST TYPE")
S0FP=(filterS0FP).groupby(datas0['TestType']).sum()
print("S0")
print(S0FP)
S1FP=(filterS1FP).groupby(datas1['TestType']).sum()
print("S1")
print(S1FP)
S2FP=(filterS2FP).groupby(datas2['TestType']).sum()
print("S2")
print(S2FP)
AttFP=(filterAttFP).groupby(dataatt['TestType']).sum()
print("Att")
print(AttFP)

# True Negative: target 0 e FINISH_CERTIFIED
# True Positive: target >0 e !FINISH_CERTIFIED
filterS0TN = (datas0["FinalRes"]!="FINISH_CERTIFIED") & (datas0["Target"]>0)
filterS0TP = (datas0["FinalRes"]=="FINISH_CERTIFIED") & (datas0["Target"]>=0)
countfn = datas0["Target"][filterS0TN].count()
countfp = datas0["Target"][filterS0TP].count()
print("True negative S0 " +  str(countfn))
print("True positive S0 " +  str(countfp))

filterS1TN = (datas1["FinalRes"]!="FINISH_CERTIFIED") & (datas1["Target"]>0)
filterS1TP = (datas1["FinalRes"]=="FINISH_CERTIFIED") & (datas1["Target"]>=0)
countfn = datas1["Target"][filterS1TN].count()
countfp = datas1["Target"][filterS1TP].count()
print("True negative S1 " + str(countfn))
print("True positive S1 " +  str(countfp))

filterS2TN = (datas2["FinalRes"]!="FINISH_CERTIFIED") & (datas2["Target"]>0)
filterS2TP = (datas2["FinalRes"]=="FINISH_CERTIFIED") & (datas2["Target"]>=0)
countfn = datas2["Target"][filterS2TN].count()
countfp = datas2["Target"][filterS2TP].count()
print("True negative S2 " + str(countfn))
print("True positive S2 " +  str(countfp))

filterAttTN = (dataatt["FinalRes"]!="FINISH_CERTIFIED") & (dataatt["Target"]>0)
filterAttTP = (dataatt["FinalRes"]=="FINISH_CERTIFIED") & (dataatt["Target"]>=0)
countfn = dataatt["Target"][filterAttTN].count()
countfp = dataatt["Target"][filterAttTP].count()
print("True negative Attenzione " + str(countfn))
print("True positive Attenzione " +  str(countfp))

# True Negative: target 0 e FINISH_CERTIFIED -> GROUPED BY TEST TYPE
print("TRUE NEGATIVE GROUPED BY TEST TYPE")
S0TN=(filterS0TN).groupby(datas0['TestType']).sum()
print("S0")
print(S0TN)
S1TN=(filterS1TN).groupby(datas1['TestType']).sum()
print("S1")
print(S1TN)
S2TN=(filterS2TN).groupby(datas2['TestType']).sum()
print("S2")
print(S2TN)
AttTN=(filterAttTN).groupby(dataatt['TestType']).sum()
print("Att")
print(AttTN)

# True Positive: target >0 e !FINISH_CERTIFIED -> GROUPED BY TEST TYPE
print("TRUE POSITIVE GROUPED BY TEST TYPE")
S0TP=(filterS0TP).groupby(datas0['TestType']).sum()
print("S0")
print(S0TP)
S1TP=(filterS1TP).groupby(datas1['TestType']).sum()
print("S1")
print(S1TP)
S2TP=(filterS2TP).groupby(datas2['TestType']).sum()
print("S2")
print(S2TP)
AttTP=(filterAttTP).groupby(dataatt['TestType']).sum()
print("Att")
print(AttTP)

############################
# Reserch question 2
# number of steps
stepS0=datas0.groupby(['TestType'])['Steps'].mean()
print(stepS0)
stepS1=datas1.groupby(['TestType'])['Steps'].mean()
print(stepS1)
stepS2=datas2.groupby(['TestType'])['Steps'].mean()
print(stepS2)
stepAtt=dataatt.groupby(['TestType'])['Steps'].mean()
print(stepAtt)

############################
# Reserch question 3
# algorithm that guarantees measured level equals to target level
certificationCorrectS0 = (datas0["FinalRes"]=="FINISH_CERTIFIED") & (datas0["Target"]==datas0["Level"])
countCertS0 = datas0["Target"][certificationCorrectS0].groupby(datas0['TestType']).count()
print("Certified = Target S0 " +  str(countCertS0))


certificationCorrectS1 = (datas1["FinalRes"]=="FINISH_CERTIFIED") & (datas1["Target"]==datas1["Level"])
countCertS1 = datas1["Target"][certificationCorrectS1].groupby(datas1['TestType']).count()
print("Certified = Target S1 " +  str(countCertS1))

certificationCorrectS2 = (datas2["FinalRes"]=="FINISH_CERTIFIED") & (datas2["Target"]==datas2["Level"])
countCertS2 = datas2["Target"][certificationCorrectS2].groupby(datas2['TestType']).count()
print("Certified = Target S2 " +  str(countCertS2))

certificationCorrectatt = (dataatt["FinalRes"]=="FINISH_CERTIFIED") & (dataatt["Target"]==dataatt["Level"])
countCertatt = dataatt["Target"][certificationCorrectatt].groupby(dataatt['TestType']).count()
print("Certified = Target Att " +  str(countCertatt))


############################
# Reserch question 4
# algorithm with the minimum difference between target level and measured level
datas0['DiffTargetCert'] = datas0["Target"]-datas0["Level"]
countDiffMinS0=datas0[(datas0["Target"]!=0)].groupby(['TestType','DiffTargetCert']).count()
print("Certified = S0 \n" + str(countDiffMinS0))

datas1['DiffTargetCert'] = datas1["Target"]-datas1["Level"]
countDiffMinS1=datas1[(datas1["Target"]!=0)].groupby(['TestType','DiffTargetCert']).count()
print("Certified = S1 \n" + str(countDiffMinS1))

datas2['DiffTargetCert'] = datas2["Target"]-datas2["Level"]
countDiffMinS2=datas2[(datas2["Target"]!=0)].groupby(['TestType','DiffTargetCert']).count()
print("Certified = S2 \n" + str(countDiffMinS2))
'''
dataatt['DiffTargetCert'] = dataatt["Target"]-dataatt["Level"]
countDiffMinatt=dataatt[(dataatt["Target"]!=0)].groupby(['TestType','DiffTargetCert']).count()
print("Certified = Target Att \n" + str(countDiffMinatt.to_string()))
countDiffMinatt2=countDiffMinatt.loc['BESTN']
print("Certified = Target Att \n" + str(countDiffMinatt2.to_string()))
countDiffMinatt2.plot(x="DiffTargetCert", y="Level",kind="bar")
#hist = countDiffMinatt2.hist('Level')




