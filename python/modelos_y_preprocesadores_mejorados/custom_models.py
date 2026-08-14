import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin


class ClasificadorConUmbral(BaseEstimator, ClassifierMixin):

    def __init__(self, pipeline_base, umbral=0.40, categoria_defecto="Otros"):
        self.pipeline_base = pipeline_base
        self.umbral = umbral
        self.categoria_defecto = categoria_defecto
        self.classes_ = []

    def fit(self, X, y):
        self.pipeline_base.fit(X, y)
        base_classes = self.pipeline_base.classes_
        if self.categoria_defecto not in base_classes:
            self.classes_ = np.append(base_classes, self.categoria_defecto)
        else:
            self.classes_ = base_classes
        return self

    def predict(self, X):
        probs = self.pipeline_base.predict_proba(X)
        max_probs = np.max(probs, axis=1)
        pred_indices = np.argmax(probs, axis=1)
        clases_originales = self.pipeline_base.classes_
        resultados = []
        for p_max, idx in zip(max_probs, pred_indices):
            if p_max < self.umbral:
                resultados.append(self.categoria_defecto)
            else:
                resultados.append(clases_originales[idx])
        return np.array(resultados)

    def predict_proba(self, X):
        return self.pipeline_base.predict_proba(X)

    def get_prediction_details(self, X):
        probs = self.pipeline_base.predict_proba(X)
        max_probs = np.max(probs, axis=1)
        pred_indices = np.argmax(probs, axis=1)
        clases_originales = self.pipeline_base.classes_
        results = []
        for i in range(len(X)):
            p_max = max_probs[i]
            idx = pred_indices[i]
            is_doubtful = p_max < self.umbral
            final_category = (
                self.categoria_defecto if is_doubtful else clases_originales[idx]
            )
            results.append(
                {
                    "categoria_final": final_category,
                    "confianza": float(p_max),
                    "es_clasificacion_dudosa": bool(is_doubtful),
                }
            )
        return results