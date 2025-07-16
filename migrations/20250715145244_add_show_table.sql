-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Show] (
    [Id]            UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Show PRIMARY KEY,
    [Title]         NVARCHAR(100)    NOT NULL,
    [Description]   NVARCHAR(1000)   NOT NULL,
    [ReleaseDate]   DATE             NOT NULL,
    [Thumbnail]     VARCHAR(MAX),
    [CreatedTime]   DATETIME         NOT NULL,
    [UpdatedTime]   DATETIME         NOT NULL,
    [OnGoing]       BIT              NOT NULL,
    [AgeRating]     VARCHAR(50)      NOT NULL,
    [IsSeries]      BIT              NOT NULL,
    [IsDeleted]     BIT              NOT NULL CONSTRAINT DF_Show_IsDeleted DEFAULT 0,
    [CommentSection]UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Show_CommentSection FOREIGN KEY REFERENCES [dbo].[CommentSection]([Id]) CONSTRAINT UQ_Show_CommentSection UNIQUE
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Show];
-- +goose StatementEnd
