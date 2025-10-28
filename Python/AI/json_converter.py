import json

class JSONConverter:
    def __init__(self, text) -> None:
        self.text = str(text or "").strip()
        self.exception = None

    def convert_to_json(self):
        print(self.text)
        if self.text.startswith("```json") or self.text.startswith("```JSON"):
            self.text = self.text[self.text.find("{"):]
        elif self.text.startswith("```"):
            self.text = self.text[self.text.find("{"):]

        try:
            data = json.loads(self.text.strip("`"))
            if isinstance(data, dict):
                # Проверяем наличие ключей
                data.setdefault("title", "Untitled")
                data.setdefault("message", "")
                return data
            else:
                print("JSON warning: data is not dict")
                return {"title": "Untitled", "message": str(data)}
        except Exception as e:
            print("Not a JSON message, trying to convert by additional request")
            self.exception = "NotJSON"
            return {"title": "Untitled", "message": self.text}

    def check_exception(self):
        return self.exception

    def __repr__(self):
        return json.dumps(self.convert_to_json(), ensure_ascii=False, indent=2)