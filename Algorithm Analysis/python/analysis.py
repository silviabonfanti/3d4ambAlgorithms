import numpy as  np
import pandas as pd

data15=pd.read_excel("test1complete.xlsx", "Simulation15", engine="openpyxl")
data15.set_index("idPatient", inplace=True)
data15=data15.apply(pd.to_numeric, downcast='integer', errors='ignore')
print(data15.info())
print(data15.head(5))