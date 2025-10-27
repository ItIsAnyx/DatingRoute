import json

class JSONConverter:
    def __init__(self, text) -> None:
        self.text = text

    def convert_to_json(self):
        try:
            if self.text[0] == '`' or 'j' or 'J':
                self.text = self.text[self.text.find("{"):]
            data = json.loads(self.text.strip("`"))
            if isinstance(data, dict):
                return data
            else:
                print("JSON warning: data is not dict")
                return {"title": "Untitled", "message": str(data)}
        except json.JSONDecodeError:
            print("JSON exception")
            return {"title": "Auto title", "message": self.text}

    def __repr__(self):
        return json.dumps(self.convert_to_json(), ensure_ascii=False, indent=2)